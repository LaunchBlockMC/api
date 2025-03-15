package gg.launchblock.api.entities;

import gg.launchblock.api.constants.Permission;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;
import java.util.Set;

@Data
@Accessors(chain = true)
@MongoEntity(collection = "tokens", database = "api")
public class TokenEntity {

    @BsonId
    private String identifier;

    private String clientIdentifier;
    private String workspaceIdentifier;
    private String environmentIdentifier;

    // user entered meta
    private String name;
    private String description;

    private String token;

    private boolean isContainer; // container api token?
    private Instant expiryTimestamp;

    // permissions
    private Set<Permission> scope;

    public Set<Permission> getScope() {
        if (this.isContainer()) {
            this.scope.add(Permission.CONTAINER);
        }
        this.scope.addAll(Set.of(Permission.VIEW_WORKSPACE, Permission.VIEW_API_TOKENS));
        return this.scope;
    }

}
