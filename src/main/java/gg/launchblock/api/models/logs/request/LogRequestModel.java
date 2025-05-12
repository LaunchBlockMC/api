package gg.launchblock.api.models.logs.request;

import gg.launchblock.api.models.lifecycles.LifecycleStage;
import gg.launchblock.api.models.logs.LogLevel;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Data
public class LogRequestModel {

    @Schema(description = "The log message content")
    private String message;

    @Schema(description = "The severity level of the log entry")
    private LogLevel level;

    private LifecycleStage stage;

    @Schema(description = "Timestamp when the log entry was created", example = "2024-03-20T10:15:30Z")
    private Instant timestamp;

}
