package gg.launchblock.api.models.lifecycles;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectRestartModel {

    private ModelRestartType type;
    private Integer maxTries;

}
