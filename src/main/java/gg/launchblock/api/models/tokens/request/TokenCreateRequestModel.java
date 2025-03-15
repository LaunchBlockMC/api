package gg.launchblock.api.models.tokens.request;

import gg.launchblock.api.constants.Permission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Data
public class TokenCreateRequestModel {

    @Size(max = 256, min = 3)
    @NotNull
    @Schema(maxLength = 256, minLength = 3, description = "The of your API token", example = "Discord Bot")
    private String name;

    @Size(max = 256, min = 3)
    @NotNull
    @Schema(maxLength = 256, minLength = 3, description = "A description of your API token", example = "To create containers with a discord command")
    private String description;

    @NotNull
    private Instant expiryTimestamp = Instant.now().plus(30, ChronoUnit.DAYS);

    @NotNull
    @NotEmpty
    private Set<Permission> scope;

    @Schema(hidden = true)
    private boolean isContainer;

    @Schema(hidden = true)
    private UUID forcedIdentifier;

}
