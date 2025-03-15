package gg.launchblock.api.models.github.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GitRepositoryResponseModel {

    private String link;
    private String description;
    private String name;
    private String fullName;
    private boolean isPrivate;
    private String defaultBranch;

    private String username; // or org

}
