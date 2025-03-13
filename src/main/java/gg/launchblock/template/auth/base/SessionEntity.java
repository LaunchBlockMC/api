package gg.launchblock.template.auth.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SessionEntity {

    @BsonId
    private String identifier; // clientID
    private long expiry;

    // cached at the start of login session : workspace - permission set
    private Map<String, Set<String>> workspacePermissions;

    // cached at start of login session : workspace - priority
    private Map<String, Integer> highestGroupPriorities;

    @JsonIgnore
    public boolean isApplication() {
        return this.workspacePermissions.getOrDefault("00000000-0000-0000-0000-000000000000", Set.of())
                .contains("application");
    }

    @JsonIgnore
    public boolean isExpired() {
        return (System.currentTimeMillis() / 1000) > this.expiry;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}