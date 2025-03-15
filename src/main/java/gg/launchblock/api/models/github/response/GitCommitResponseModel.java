package gg.launchblock.api.models.github.response;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Data
public class GitCommitResponseModel {

    @Schema(description = "Commit identifier (hash)")
    private String identifier;

    @Schema(description = "The user who made the commit")
    private String username;

    @Schema(description = "The title of the commit", example = "feat: added a new island to the list of schematics")
    private String title;

    @Schema(description = "The avatar URL of the user who made the commit")
    private String avatarUrl;
    
    private Instant timestamp;

}
