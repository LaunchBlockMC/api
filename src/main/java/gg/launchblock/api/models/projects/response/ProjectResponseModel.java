package gg.launchblock.api.models.projects.response;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Data
public class ProjectResponseModel {

    @Schema(description = "The identifier of the project")
    private UUID identifier;

    @Size(min = 3, max = 50)
    @Schema(description = "A name for your project", example = "Discord Bot")
    private String name;

    @Schema(description = "The URL of an icon you'd like to give your project")
    private String iconUrl;

    @Schema(description = "If this project is a game (Minecraft) project", defaultValue = "false")
    private boolean game = false;

    @Schema(description = "The icon type for your game, shown in the in-game menu", defaultValue = "DIAMOND_SWORD")
    private String iconItemType = "DIAMOND_SWORD";

    @Schema(description = "The link to the github repository to listen for updates on",
            example = "https://github.com/LaunchBlockMC/example-skywars")
    private String githubRepository;

    @Schema(description = "The GitHub branch we should listen for changes on", example = "main")
    private String githubBranch;

    @Schema(description = "If the project can be removed from the environment", defaultValue = "true")
    private boolean removable;

}
