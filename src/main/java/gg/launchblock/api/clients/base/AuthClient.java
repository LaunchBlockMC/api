package gg.launchblock.api.clients.base;

import gg.launchblock.api.auth.base.SessionEntity;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.base.request.AuthVerificationRequest;
import gg.launchblock.api.models.base.response.PermissionListResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static gg.launchblock.api.auth.base.TokenValidator.tokenToSession;

@ApplicationScoped
public class AuthClient {

    /**
     * Get a list of permissions a user has in a given workspace workspace Responds with a 200
     * response and a list of permissions if everything is valid, 403 if not
     *
     * @param permissionRequest the permission request containing user information
     * @return a successful or failed uni depending on the result
     */
    public Uni<PermissionListResponse> permissionList(final AuthVerificationRequest permissionRequest) {
        final String token = permissionRequest.getToken();
        final SessionEntity session = tokenToSession(token);

        this.verifySession(session, permissionRequest.getUserIdentifier().toString());

        final UUID workspace = this.getWorkspace(session, permissionRequest);
        final UUID user = permissionRequest.getUserIdentifier();

        // get permissions in given workspace
        final Set<String> permissions = session.getWorkspacePermissions().getOrDefault(workspace.toString(), new HashSet<>());

        final int priority = session.getHighestGroupPriorities().getOrDefault(workspace.toString(), 0);
        return Uni.createFrom().item(new PermissionListResponse(permissionRequest.getWorkspaceIdentifier(), user, permissions, priority));
    }

    private UUID getWorkspace(final SessionEntity session, final AuthVerificationRequest verificationRequest) {
        return session.isApplication() ? UUID.fromString("00000000-0000-0000-0000-000000000000") : verificationRequest.getWorkspaceIdentifier();
    }

    private void verifySession(final SessionEntity session, final String userIdentifier) {
        // if session is expired - or the wrong user vs header, then error
        if (session.isExpired() || !session.getIdentifier().equals(userIdentifier)) {
            throw new LaunchBlockException(BuiltInExceptions.EXPIRED_INVALID_JWT);
        }
    }

}