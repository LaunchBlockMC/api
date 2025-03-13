package gg.launchblock.template.user.base;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Optional;

import static gg.launchblock.template.constants.AuthConstants.LAUNCH_BLOCK_ACTOR;

@Setter
@Getter
@RequestScoped
public class RequestContextHolder {

    private ContainerRequestContext requestContext;

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