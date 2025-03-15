package gg.launchblock.api.models.lifecycles;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectScalingModel {

    private ModelScalingType type;
    private Integer amount;
    private Integer min;
    private Integer max;
    private Integer cpuThreshold;
    private Integer memoryThreshold;
    private Integer minPlayers;
    private Integer maxPlayers;


}
