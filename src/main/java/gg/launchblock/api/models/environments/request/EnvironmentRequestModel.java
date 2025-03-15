package gg.launchblock.api.models.environments.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Accessors(chain = true)
public class EnvironmentRequestModel {

    @Size(min = 3, max = 30)
    @NotNull
    @Schema(description = "The name of the environment", example = "staging")
    private String name;

}

