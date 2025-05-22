package gg.launchblock.api.models.matchmaker.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Data
public class MatchSubmitRequest {

    @NotNull
    @Schema(description = "The identifier of a project within your workspace")
    private UUID projectIdentifier;

    @NotNull
    @NotEmpty
    private List<UUID> players;

} 