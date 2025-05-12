package gg.launchblock.api.models.logs.response;

import gg.launchblock.api.models.lifecycles.LifecycleStage;
import gg.launchblock.api.models.logs.LogLevel;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Data
public class LogResponseModel {

    @Schema(description = "Unique identifier for the lifecycle instance", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID lifecycleIdentifier;

    @Schema(description = "Unique identifier for the container instance", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID containerIdentifier;

    @Schema(description = "The log message content")
    private String message;

    @Schema(description = "The severity level of the log entry")
    private LogLevel level;

    private LifecycleStage stage;

    @Schema(description = "Timestamp when the log entry was created", example = "2024-03-20T10:15:30Z")
    private Instant timestamp;

}
