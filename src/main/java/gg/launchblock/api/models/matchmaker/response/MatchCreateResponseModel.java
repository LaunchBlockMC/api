package gg.launchblock.api.models.matchmaker.response;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

/**
 * Response object for match submission requests.
 */
@Data
public class MatchCreateResponseModel {

    private UUID matchGroupIdentifier;

    @Schema(description = "The identifier of the game instance that the match is tied to (if applicable)")
    private UUID gameIdentifier;

    @Schema(description = "The type of result that was returned from the match submission")
    private MatchRequestResult resultType;
} 