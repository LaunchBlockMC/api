package gg.launchblock.template.clients.base;

import gg.launchblock.template.auth.base.SessionEntity;
import gg.launchblock.template.exception.base.BuiltInExceptions;
import gg.launchblock.template.exception.base.LaunchBlockException;
import gg.launchblock.template.models.base.request.AuthTokenRequest;
import gg.launchblock.template.models.base.request.AuthVerificationRequest;
import gg.launchblock.template.models.base.response.AuthTokenResponse;
import gg.launchblock.template.models.base.response.PermissionListResponse;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static gg.launchblock.template.auth.base.TokenValidator.tokenToSession;

@RegisterRestClient(configKey = "auth")
public interface AuthClient {

    /**
     * Get a list of permissions a user has in a given workspace workspace Responds with a 200
     * response and a list of permissions if everything is valid, 403 if not
     *
     * @param permissionRequest the permission request containing user information
     * @return a successful or failed uni depending on the result
     */
    default Uni<PermissionListResponse> permissionList(final AuthVerificationRequest permissionRequest) {
        final String token = permissionRequest.getToken();
        final SessionEntity session = tokenToSession(token);
        if (session.isExpired() || !session.getIdentifier().equals(permissionRequest.getUserIdentifier().toString())) {
            throw new LaunchBlockException(BuiltInExceptions.EXPIRED_INVALID_JWT);
        }
        final UUID workspace = session.isApplication() ? UUID.fromString("00000000-0000-0000-0000-000000000000") : permissionRequest.getWorkspaceIdentifier();
        final UUID user = permissionRequest.getUserIdentifier();
        final Set<String> permissions = session.getWorkspacePermissions().getOrDefault(workspace.toString(), new HashSet<>());
        final int priority = session.getHighestGroupPriorities().getOrDefault(workspace.toString(), 0);
        return Uni.createFrom().item(new PermissionListResponse(permissionRequest.getWorkspaceIdentifier(), user, permissions, priority));
    }

    @POST
    @Path("/accounts/login")
    @CacheResult(cacheName = "auth-login")
    Uni<AuthTokenResponse> login(final AuthTokenRequest authTokenRequest);

}