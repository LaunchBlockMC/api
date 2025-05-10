package gg.launchblock.api.models.scaling;

import lombok.Data;

import java.util.Map;

@Data
public class ScalingGoalResponseModel {

    private Map<String, Boolean> actionRequired;

    private int onlineContainers;
    private int startingContainers;
    private int stoppingContainers;

}
