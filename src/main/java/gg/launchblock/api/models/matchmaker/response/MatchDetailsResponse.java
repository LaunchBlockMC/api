package gg.launchblock.api.models.matchmaker.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class MatchDetailsResponse {
    private UUID identifier;
    private UUID gameIdentifier;
    private UUID projectIdentifier;

    private int minPlayers;
    private int maxPlayers;
    private int playerCount = 0;

    private List<UUID> players;
} 