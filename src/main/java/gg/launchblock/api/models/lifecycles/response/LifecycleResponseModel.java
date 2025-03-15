package gg.launchblock.api.models.lifecycles.response;

import gg.launchblock.api.models.lifecycles.LifecycleStage;
import gg.launchblock.api.models.lifecycles.LifecycleState;
import gg.launchblock.api.models.lifecycles.LifecycleStateLogModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Accessors(chain = true)
public class LifecycleResponseModel {

    @Schema(description = "The identifier of this lifecycle")
    private String identifier;

    @Schema(description = "A GitHub commit attached to this lifecycle", nullable = true)
    private String commit;

    @Schema(description = "The docker image generated as part of the build stage", nullable = true)
    private String image;

    @Schema(description = "A log of state changes that have happened to this lifecycle")
    private List<LifecycleStateLogModel> stateLog;

    @Schema(description = "The current state of the lifecycle")
    public LifecycleState state;

    @Schema(description = "The current stage of the lifecycle")
    public LifecycleStage stage;

    public String getBuildLogs() {
        return "https://api.launchblock.gg/v1/logs/" + this.identifier + "?lifecycle_stage=build";
    }

    public String getDeployLogs() {
        return "https://api.launchblock.gg/v1/logs/" + this.identifier + "?lifecycle_stage=deploy";
    }

}
