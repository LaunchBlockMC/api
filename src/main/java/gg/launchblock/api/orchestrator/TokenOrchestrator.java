package gg.launchblock.api.orchestrator;

import com.google.common.base.Preconditions;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.entities.TokenEntity;
import gg.launchblock.api.mapper.TokenMapper;
import gg.launchblock.api.models.tokens.request.TokenCreateRequestModel;
import gg.launchblock.api.models.tokens.response.ExposedTokenResponseModel;
import gg.launchblock.api.models.tokens.response.TokenResponseModel;
import gg.launchblock.api.service.TokenService;
import gg.launchblock.api.user.base.LaunchBlockActor;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class TokenOrchestrator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TokenService tokenService;
    private final TokenMapper tokenMapper;
    private final RequestContextHolder contextHolder;

    public Uni<ExposedTokenResponseModel> createToken(final String workspaceIdentifier, final String environmentIdentifier, final TokenCreateRequestModel requestModel) {

        final LaunchBlockActor actor = this.contextHolder.getUser();
        final Set<String> permissions = requestModel.getScope().stream().map(Permission::getIdentifier).collect(Collectors.toSet());

        Preconditions.checkArgument(requestModel.getExpiryTimestamp().isBefore(Instant.now().plus(Duration.of(183, ChronoUnit.DAYS))),
                "Token cannot be set to expire in more than 6 months time");
        Preconditions.checkArgument(requestModel.getScope().stream().allMatch(Permission::isCanAddToToken) || actor.hasPermission(workspaceIdentifier, "application"),
                "Permissions must all be assignable to an API token");
        Preconditions.checkArgument(actor.hasPermissions(workspaceIdentifier, permissions),
                "You must have all of the permissions you're trying to give to an API token");
        Preconditions.checkArgument(!requestModel.isContainer() || actor.hasPermission(workspaceIdentifier, "application"),
                "You don't have permission to set this as a container key, only our system can do this!");
        Preconditions.checkArgument(requestModel.getForcedIdentifier() == null || actor.hasPermissions(workspaceIdentifier, Set.of("application", "create_api_tokens")),
                "You don't have permission to set this as a container key, only our system can do this!");

        final TokenEntity generatedEntity = new TokenEntity()
                .setWorkspaceIdentifier(workspaceIdentifier)
                .setEnvironmentIdentifier(environmentIdentifier)
                .setName(requestModel.getName())
                .setDescription(requestModel.getDescription())
                .setExpiryTimestamp(requestModel.getExpiryTimestamp())
                .setIdentifier(requestModel.getForcedIdentifier() == null ?
                        UUID.randomUUID().toString() : requestModel.getForcedIdentifier().toString())
                .setScope(requestModel.getScope())
                .setToken(TokenOrchestrator.generateRandomString());

        return this.tokenService.createToken(generatedEntity)
                .map(this.tokenMapper::toExposedToken);

    }

    private static String generateRandomString() {
        final int length = TokenOrchestrator.RANDOM.nextInt((50 - 40) + 1) + 40;
        final StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(TokenOrchestrator.CHARACTERS.charAt(TokenOrchestrator.RANDOM.nextInt(TokenOrchestrator.CHARACTERS.length())));
        }

        return sb.toString();
    }

    public Uni<List<TokenResponseModel>> getTokens(final String workspaceIdentifier, final String environmentIdentifier, final boolean showContainers) {
        // todo - create index on this query
        return this.tokenService.getTokens(workspaceIdentifier, environmentIdentifier)
                .map(tokens -> {
                    if (!showContainers) {
                        return tokens.stream().filter(t -> !t.isContainer()).toList();
                    }
                    return tokens;
                }).map(this.tokenMapper::toModels);
    }

    public Uni<TokenResponseModel> getToken(final String workspaceIdentifier, final String environmentIdentifier, final String identifier) {
        return this.tokenService.getToken(workspaceIdentifier, environmentIdentifier, identifier)
                .map(this.tokenMapper::toModel);
    }

    public Uni<Void> deleteToken(final String workspaceIdentifier, final String environmentIdentifier, final String identifier) {
        return this.tokenService.deleteToken(workspaceIdentifier, environmentIdentifier, identifier);
    }

}
