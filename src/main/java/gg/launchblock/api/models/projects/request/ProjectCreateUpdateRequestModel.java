package gg.launchblock.api.models.projects.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Data
public class ProjectCreateUpdateRequestModel {

    @Size(min = 3, max = 50)
    @NotNull
    @Schema(description = "A name for your project", example = "Discord Bot")
    private String name;

    @Schema(description = "The URL of an icon you'd like to give your project", nullable = true)
    private String iconUrl;

    @Schema(description = "If this project is a game (Minecraft) project", defaultValue = "false")
    private boolean game = false;

    @Schema(description = "The icon type for your game, shown in the in-game menu", defaultValue = "DIAMOND_SWORD", nullable = true)
    private String iconItemType = "DIAMOND_SWORD";

    @Schema(description = "The link to the github repository to listen for updates on",
            example = "https://github.com/LaunchBlockMC/example-skywars", nullable = true)
    private String githubRepository;

    @Schema(description = "The GitHub branch we should listen for changes on", example = "main", nullable = true)
    private String githubBranch;

    @Schema(description = "The identifier of the environment to create the project in")
    private UUID environmentIdentifier;

}
