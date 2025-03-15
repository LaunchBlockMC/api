package gg.launchblock.api.models.lifecycles;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectLimitsModel {

    private int cpu = 1;
    private int memoryGB = 4;

}
