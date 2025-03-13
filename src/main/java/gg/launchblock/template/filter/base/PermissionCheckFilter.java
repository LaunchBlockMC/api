package gg.launchblock.template.filter.base;


import gg.launchblock.template.annotations.base.permissions.RequiredPermission;
import gg.launchblock.template.clients.base.AuthClient;
import gg.launchblock.template.constants.AuthConstants;
import gg.launchblock.template.exception.base.BuiltInExceptions;
import gg.launchblock.template.exception.base.LaunchBlockException;
import gg.launchblock.template.models.base.request.AuthVerificationRequest;
import gg.launchblock.template.user.base.LaunchBlockActor;
import gg.launchblock.template.user.base.RequestContextHolder;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.lang.reflect.Method;
import java.util.*;

import static gg.launchblock.template.constants.AuthConstants.LAUNCH_BLOCK_ACTOR;
import static gg.launchblock.template.exception.base.BuiltInExceptions.ESSENTIAL_HEADERS_MISSING;


@RequestScoped
@Provider
@RequiredArgsConstructor
public class PermissionCheckFilter {

    @Inject
    @RestClient
    AuthClient authClient;

    @Inject
    RequestContextHolder requestContextHolder;

    @Context
    ResourceInfo resourceInfo;

    protected static Optional<RequiredPermission> getAnnotation(final Method method) {
        return Optional.ofNullable(method.getAnnotation(RequiredPermission.class));
    }

    protected Method getMethod() {
        return this.resourceInfo.getResourceMethod();
    }

    @ServerRequestFilter(priority = 10000)
    public Uni<Void> getFilter(final ContainerRequestContext ctx) {

        this.requestContextHolder.setRequestContext(ctx);

        final Method method = this.getMethod();
        final Optional<RequiredPermission> requiredPermissionOptional = PermissionCheckFilter.getAnnotation(method);

        // get list of permissions if annotation exists
        final Set<String> permissionsRequires = requiredPermissionOptional.map(requiredPermission -> new HashSet<>(Arrays.stream(requiredPermission.value()).toList())).orElseGet(HashSet::new);
        if (requiredPermissionOptional.isPresent() && requiredPermissionOptional.get().applicationOnly()) {
            permissionsRequires.add("application");
        }

        final UUID userIdentifier = ctx.getHeaderString(AuthConstants.USER_IDENTIFIER) == null ? null : UUID.fromString(ctx.getHeaderString(AuthConstants.USER_IDENTIFIER));
        final String userToken = ctx.getHeaderString(AuthConstants.USER_TOKEN);
        final UUID workspaceIdentifier = ctx.getHeaderString(AuthConstants.WORKSPACE_IDENTIFIER) == null ? null : UUID.fromString(ctx.getHeaderString(AuthConstants.WORKSPACE_IDENTIFIER));

        if (userToken == null || userIdentifier == null || workspaceIdentifier == null) {

            if (permissionsRequires.isEmpty()) {
                Log.warn(method.getName() + " is not configured to check permissions and no auth headers came in, beware!");
                return Uni.createFrom().voidItem();
            }

            Log.error("Permission was required but essential headers weren't provided");
            throw new LaunchBlockException(ESSENTIAL_HEADERS_MISSING);
        }

        // make request via authclient to fetch permissions of user
        return this.authClient
                .permissionList(
                        AuthVerificationRequest.builder()
                                .userIdentifier(userIdentifier)
                                .token(userToken)
                                .workspaceIdentifier(workspaceIdentifier)
                                .build())
                .invoke(
                        permissionListResponse -> {
                            final boolean isApplication =
                                    permissionListResponse.getPermissions().contains("application");

                            final LaunchBlockActor launchBlockActor = new LaunchBlockActor(!isApplication, permissionListResponse.getUserIdentifier());
                            launchBlockActor.setPermissions(permissionListResponse.getWorkspaceIdentifier().toString(), permissionListResponse.getPermissions());
                            launchBlockActor.setHighestGroupPriority(permissionListResponse.getWorkspaceIdentifier().toString(), permissionListResponse.getHighestGroupPriority());

                            // store permissions in request context
                            ctx.setProperty(
                                    LAUNCH_BLOCK_ACTOR,
                                    launchBlockActor);

                            // verify request contains given permission list
                            if (!launchBlockActor.hasPermissions(String.valueOf(workspaceIdentifier), permissionsRequires)) {
                                Log.error("User " + userIdentifier + " does not have permissions " + permissionsRequires + " for " + method.getName() + " in workspace " + workspaceIdentifier);
                                throw new LaunchBlockException(BuiltInExceptions.NO_PERMISSION, userIdentifier + " does not have permission", String.join(", ", permissionsRequires));
                            }

                        })
                .replaceWithVoid();
    }
}