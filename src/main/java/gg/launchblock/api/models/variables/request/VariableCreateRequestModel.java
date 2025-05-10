package gg.launchblock.api.models.variables.request;

import java.time.Instant;

public class VariableCreateRequestModel {

    private String name;
    private boolean secret;
    private String content;
    private Instant expiryTimestamp;

}
