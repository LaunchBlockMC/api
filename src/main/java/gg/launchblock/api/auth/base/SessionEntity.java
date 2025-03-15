package gg.launchblock.api.auth.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.entities.TokenEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SessionEntity {

    private String identifier;
    private long expiry;
    private Map<String, Set<String>> workspacePermissions;
    private Map<String, Integer> highestGroupPriorities;

    @JsonIgnore
    public boolean isApplication() {
        return this.workspacePermissions.getOrDefault("00000000-0000-0000-0000-000000000000", Set.of())
                .contains("application");
    }

    @JsonIgnore
    public boolean isToken() {
        return this.workspacePermissions.values().stream().anyMatch(permissions -> permissions.contains("api_token"));
    }

    @JsonIgnore
    public boolean isContainer() {
        return this.workspacePermissions.values().stream().anyMatch(permissions -> permissions.contains("container"));
    }

    @JsonIgnore
    public boolean isExpired() {
        return (System.currentTimeMillis() / 1000) > this.expiry;
    }

    public static SessionEntity fromToken(final TokenEntity token) {
        final Map<String, Set<String>> permissions = Map.of(token.getWorkspaceIdentifier(), new HashSet<>(token.getScope().stream()
                .map(Permission::getIdentifier).collect(Collectors.toSet())));
        permissions.get(token.getWorkspaceIdentifier()).add("api_token");
        return new SessionEntity()
                .setExpiry(token.getExpiryTimestamp().getEpochSecond() * 1000)
                .setIdentifier(token.getIdentifier())
                .setHighestGroupPriorities(Map.of(token.getWorkspaceIdentifier(), 1))
                .setWorkspacePermissions(permissions);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}