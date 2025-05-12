package gg.launchblock.api.models.variables.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariableResponseModel {

    private String identifier;
    private String projectIdentifier;
    private String lifecycleIdentifier;

    @Schema(description = "The name of the variable to insert into containers", examples = "PORT")
    private String name;

    @Schema(description = "To hide the contents of the variable in responses")
    private boolean secret;

    private String content;
    private Instant expiryTimestamp;
    private boolean isExpired;

    private VariableSource source;

} 