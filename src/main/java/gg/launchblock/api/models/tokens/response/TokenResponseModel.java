package gg.launchblock.api.models.tokens.response;

import gg.launchblock.api.constants.Permission;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class TokenResponseModel {

    private UUID identifier;
    private String name;
    private String description;
    private UUID workspaceIdentifier;
    private UUID environmentIdentifier;
    private Instant expiryTimestamp;
    private Set<Permission> scope;

}
