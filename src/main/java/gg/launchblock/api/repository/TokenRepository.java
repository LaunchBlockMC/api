package gg.launchblock.api.repository;

import gg.launchblock.api.auth.base.ApiTokenValidator;
import gg.launchblock.api.entities.TokenEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import java.util.List;

@ApplicationScoped
public class TokenRepository implements ReactivePanacheMongoRepositoryBase<TokenEntity, String> {

    public Uni<TokenEntity> findByUnencryptedToken(final String unencryptedToken) {
        try {
            final String encryptedToken = ApiTokenValidator.encrypt(unencryptedToken);
            return this.find("token", encryptedToken).firstResult();
        } catch (final Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    public Uni<TokenEntity> persistToken(final TokenEntity entity) {
        try {
            // Encrypt the token before persisting
            final String unencryptedToken = entity.getToken();
            final String encryptedToken = ApiTokenValidator.encrypt(unencryptedToken);
            entity.setToken(encryptedToken);
            return this.persist(entity)
                    .invoke(e -> e.setToken(unencryptedToken));
        } catch (final Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    public Uni<List<TokenEntity>> list(final Document query) {
        return this.find(query).list().map(entities -> {
            entities.forEach(entity -> {
                try {
                    // Decrypt each token in the list
                    final String decryptedToken = ApiTokenValidator.decrypt(entity.getToken());
                    entity.setToken(decryptedToken);
                } catch (final Exception e) {
                    throw new RuntimeException("Failed to decrypt token in list", e);
                }
            });
            return entities;
        });
    }
}