package gg.launchblock.template.models.base.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthTokenResponse {

    private final UUID userIdentifier;
    private final String token;
    private final long expiryTimestamp;

}
