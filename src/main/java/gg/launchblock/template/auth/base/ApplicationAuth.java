package gg.launchblock.template.auth.base;

import gg.launchblock.template.clients.base.AuthClient;
import gg.launchblock.template.models.base.request.AuthTokenRequest;
import gg.launchblock.template.models.base.response.AuthTokenResponse;
import gg.launchblock.template.user.base.LaunchBlockActor;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ApplicationAuth {

    @ConfigProperty(name = "application-uuid")
    String applicationUUID;

    @ConfigProperty(name = "application-secret")
    String applicationSecret;

    @ConfigProperty(name = "internal-app-domain", defaultValue = "@launchblock.gg")
    String appDomain;

    @Inject
    @RestClient
    AuthClient authClient;

    public UUID getApplicationIdentifier() {
        return UUID.fromString(this.applicationUUID);
    }

    public Uni<AuthTokenResponse> authenticateApplication() {
        return this.authClient.login(new AuthTokenRequest(UUID.fromString(this.applicationUUID), this.applicationUUID + this.appDomain, this.applicationSecret))
                .onFailure()
                .invoke(throwable -> ApplicationAuth.log.error("App failed to login", throwable));
    }

    public LaunchBlockActor getAppUser() {
        return new LaunchBlockActor(false, UUID.fromString(this.applicationUUID));
    }

}

