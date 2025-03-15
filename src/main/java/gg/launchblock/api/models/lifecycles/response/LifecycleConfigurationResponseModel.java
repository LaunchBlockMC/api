package gg.launchblock.api.models.lifecycles.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import gg.launchblock.api.models.lifecycles.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifecycleConfigurationResponseModel {

    @Schema(description = "If the definition was provided in the github config file (.launchblock/config.json)")
    private boolean fileDefined;

    @Schema(description = "Information about how to build this project", nullable = true)
    private ProjectBuildModel build;

    @Schema(description = "Information about how to deploy this project", nullable = true)
    private ProjectDeployModel deploy;

    @Schema(description = "How to handle restarts or failures in this project", nullable = true)
    private ProjectRestartModel restart;

    @Schema(description = "Limits to how much resources your project can use", nullable = true)
    private ProjectLimitsModel limits;

    @Schema(description = "Instructions on how to scale your project", nullable = true)
    private ProjectScalingModel scaling;

    @Schema(description = "Storage attachments your project needs to persist over containers", nullable = true)
    private Set<@NotNull ProjectVolumesModel> volumes;

    @Schema(description = "Information about any environment variables this project has", nullable = true)
    private Set<@NotNull ProjectEnvironmentModel> environment;

}
