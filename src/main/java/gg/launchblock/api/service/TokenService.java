package gg.launchblock.api.service;

import gg.launchblock.api.entities.TokenEntity;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.repository.TokenRepository;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Uni<TokenEntity> createToken(final TokenEntity token) {
        return this.tokenRepository.persist(token);
    }

    public Uni<List<TokenEntity>> getTokens(final String workspaceIdentifier, final String environmentIdentifier) {
        // todo - create index on this query
        return this.tokenRepository.list(
                new Document("workspaceIdentifier", workspaceIdentifier).append("environmentIdentifier", environmentIdentifier));
    }

    public Uni<TokenEntity> getToken(final String workspaceIdentifier, final String environmentIdentifier, final String identifier) {
        return this.getTokens(workspaceIdentifier, environmentIdentifier)
                .map(tokens -> tokens.stream().filter(t -> t.getIdentifier().equalsIgnoreCase(identifier)).findFirst()
                        .orElseThrow(() -> new LaunchBlockException(BuiltInExceptions.OBJECT_NOT_FOUND, "api_token")));
    }

    public Uni<Void> deleteToken(final String workspaceIdentifier, final String environmentIdentifier, final String identifier) {
        return this.getToken(workspaceIdentifier, environmentIdentifier, identifier)
                .call(this.tokenRepository::delete).replaceWithVoid();
    }

    @CacheResult(cacheName = "authorization_token_resolution")
    public Uni<TokenEntity> fromAuthorizationToken(final String authToken) {
        // todo - create index on this query
        return this.tokenRepository.find(new Document("token", authToken.replace("Bearer ", ""))).firstResult();
    }

}
