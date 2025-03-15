package gg.launchblock.api.repository;

import gg.launchblock.api.entities.TokenEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenRepository implements ReactivePanacheMongoRepositoryBase<TokenEntity, String> {
}