package gg.launchblock.template.models.base.request;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.UUID;

@Data
public class AuthTokenRequest {

    @Nullable
    private final UUID identifier;
    @Nullable
    private final String email;
    private final String password;

}
