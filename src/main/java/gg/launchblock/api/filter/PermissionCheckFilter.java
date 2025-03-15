package gg.launchblock.api.filter;


import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.auth.base.SessionEntity;
import gg.launchblock.api.auth.base.TokenGenerator;
import gg.launchblock.api.clients.base.AuthClient;
import gg.launchblock.api.constants.AuthConstants;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.entities.TokenEntity;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.base.request.AuthVerificationRequest;
import gg.launchblock.api.service.TokenService;
import gg.launchblock.api.user.base.LaunchBlockActor;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static gg.launchblock.api.constants.AuthConstants.LAUNCH_BLOCK_ACTOR;
import static gg.launchblock.api.exception.base.BuiltInExceptions.ESSENTIAL_HEADERS_MISSING;


@RequestScoped
@Provider
@RequiredArgsConstructor
public class PermissionCheckFilter {

    private final AuthClient authClient;
    private final RequestContextHolder requestContextHolder;
    private final TokenService tokenService;

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
        final Set<Permission> permissionsRequires = requiredPermissionOptional
                .map(requiredPermission -> new HashSet<>(Arrays.stream(requiredPermission.value()).toList()))
                .orElseGet(HashSet::new);

        // if annotation is containerOnly - restrict to containers
        if (requiredPermissionOptional.isPresent() && requiredPermissionOptional.get().containerOnly()) {
            permissionsRequires.add(Permission.CONTAINER);
        }

        final String authorizationToken = this.requestContextHolder.getAuthorizationToken();
        final Uni<TokenEntity> tokenEntityUni = authorizationToken == null ? Uni.createFrom().nullItem() : this.tokenService.fromAuthorizationToken(authorizationToken);

        return tokenEntityUni
                .flatMap(token -> {

                    if (token != null) {
                        this.requestContextHolder.getHeaders().add(AuthConstants.ENVIRONMENT_IDENTIFIER, token.getEnvironmentIdentifier());
                        this.requestContextHolder.getHeaders().add(AuthConstants.WORKSPACE_IDENTIFIER, token.getWorkspaceIdentifier());
                        this.requestContextHolder.getHeaders().add(AuthConstants.USER_IDENTIFIER, token.getIdentifier());
                        try {
                            this.requestContextHolder.getHeaders().add(AuthConstants.USER_TOKEN, TokenGenerator.encrypt(SessionEntity.fromToken(token).toJson()));
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    final UUID userIdentifier = this.requestContextHolder.getUserIdentifier();
                    final String userToken = this.requestContextHolder.getUserToken();
                    final UUID workspaceIdentifier = this.requestContextHolder.getWorkspaceIdentifier();

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

                                        final boolean isContainer =
                                                permissionListResponse.getPermissions().contains("container");

                                        final boolean isApiToken =
                                                permissionListResponse.getPermissions().contains("api_token");

                                        final LaunchBlockActor launchBlockActor = new LaunchBlockActor(!isApplication && !isContainer && !isApiToken, permissionListResponse.getUserIdentifier());
                                        launchBlockActor.setPermissions(permissionListResponse.getWorkspaceIdentifier().toString(), permissionListResponse.getPermissions());
                                        launchBlockActor.setHighestGroupPriority(permissionListResponse.getWorkspaceIdentifier().toString(), permissionListResponse.getHighestGroupPriority());

                                        // store permissions in request context
                                        ctx.setProperty(
                                                LAUNCH_BLOCK_ACTOR,
                                                launchBlockActor);

                                        final Set<String> permissionsRequired = permissionsRequires.stream().map(Permission::getIdentifier).collect(Collectors.toSet());

                                        // verify request contains given permission list
                                        if (!launchBlockActor.hasPermissions(String.valueOf(workspaceIdentifier), permissionsRequired)) {
                                            Log.error("User " + userIdentifier + " does not have permissions " + permissionsRequires + " for " + method.getName() + " in workspace " + workspaceIdentifier);
                                            throw new LaunchBlockException(BuiltInExceptions.NO_PERMISSION, userIdentifier + " does not have permission", String.join(", ", permissionsRequired));
                                        }

                                    })
                            .replaceWithVoid();

                });


    }
}