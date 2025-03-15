package gg.launchblock.api.models.tokens.response;

import gg.launchblock.api.constants.Permission;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class ExposedTokenResponseModel {

    private UUID identifier;
    private String name;
    private String description;
    private String token;
    private UUID workspaceIdentifier;
    private UUID environmentIdentifier;
    private Instant expiryTimestamp;
    private Set<Permission> scope;

}
