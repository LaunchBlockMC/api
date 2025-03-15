package gg.launchblock.api.models.lifecycles;

import lombok.Data;

@Data
public class ProjectEnvironmentModel {

    private String name;

    // can also contain $\{\{ MongoDB.MONGODB_USER }} variables
    private String value;

}
