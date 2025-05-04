package gg.launchblock.api.models.container.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContainerResponseModel {

    private String identifier;
    private Instant startTimestamp;

    private boolean healthy;
    private boolean online;
    private boolean removing;

    // game server specific stuff
    private String proxyIdentifier;

}
