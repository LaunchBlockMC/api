package gg.launchblock.api.models.lifecycles;

import lombok.Data;

@Data
public class ProjectBuildModel {

    private ModelBuildType builder;
    private String dockerPath;
    private String dockerImage;

}
