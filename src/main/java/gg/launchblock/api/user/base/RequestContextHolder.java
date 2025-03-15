package gg.launchblock.api.user.base;

import gg.launchblock.api.constants.AuthConstants;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static gg.launchblock.api.constants.AuthConstants.LAUNCH_BLOCK_ACTOR;

@Setter
@Getter
@RequestScoped
public class RequestContextHolder {

    private ContainerRequestContext requestContext;

    public MultivaluedMap<String, String> getHeaders() {
        return this.requestContext.getHeaders();
    }

    private String getHeaderValue(final String key) {
        final List<String> value = this.getHeaders().get(key);
        if (value == null) {
            return null;
        }
        return value.stream().findFirst().orElse(null);
    }

    public UUID getWorkspaceIdentifier() {
        final String value = this.getHeaderValue(AuthConstants.WORKSPACE_IDENTIFIER);
        return value == null ? null : UUID.fromString(value);
    }

    public UUID getUserIdentifier() {
        final String value = this.getHeaderValue(AuthConstants.USER_IDENTIFIER);
        return value == null ? null : UUID.fromString(value);
    }

    public UUID getEnvironmentIdentifier() {
        final String value = this.getHeaderValue(AuthConstants.ENVIRONMENT_IDENTIFIER);
        return value == null ? null : UUID.fromString(value);
    }

    public String getUserToken() {
        return this.getHeaderValue(AuthConstants.USER_TOKEN);
    }

    public String getAuthorizationToken() {
        return this.getHeaderValue(AuthConstants.AUTHORIZATION_HEADER);
    }

    static @NonNull Optional<LaunchBlockActor> getUserOptional(
            @NonNull final ContainerRequestContext ctx) {
        return Optional.ofNullable((LaunchBlockActor) ctx.getProperty(LAUNCH_BLOCK_ACTOR));
    }

    static @NonNull LaunchBlockActor getUser(final ContainerRequestContext containerRequestContext) {
        return RequestContextHolder.getUserOptional(containerRequestContext).orElseThrow();
    }

    public Optional<LaunchBlockActor> getUserOptional() {
        return RequestContextHolder.getUserOptional(this.requestContext);
    }

    public LaunchBlockActor getUser() {
        return RequestContextHolder.getUser(this.requestContext);
    }
}