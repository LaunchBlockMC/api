package gg.launchblock.api.models.environments.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class EnvironmentResponseModel {

    private UUID identifier;
    private String name;

}

