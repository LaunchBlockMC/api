package gg.launchblock.api.models.lifecycles.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import gg.launchblock.api.models.lifecycles.LifecycleStage;
import gg.launchblock.api.models.lifecycles.LifecycleState;
import gg.launchblock.api.models.lifecycles.LifecycleStateLogModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifecycleResponseModel {

    @Schema(description = "The identifier of this lifecycle")
    private String identifier;

    @Schema(description = "The identifier of the previous lifecycle", nullable = true)
    private String previousLifecycleIdentifier;

    @Schema(description = "A GitHub commit attached to this lifecycle", nullable = true)
    private String commit;

    @Schema(description = "A log of state changes that have happened to this lifecycle")
    private List<LifecycleStateLogModel> stateLog;

    @Schema(description = "The current state of the lifecycle")
    public LifecycleState state;

    @Schema(description = "The current stage of the lifecycle")
    public LifecycleStage stage;

}
