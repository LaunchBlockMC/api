package gg.launchblock.template.models.base.request;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@Value
public class AuthVerificationRequest {

    @NonNull
    @Schema(example = "6737eaf5-510e-4b8b-a21d-4c6ae7c1b94a")
    UUID workspaceIdentifier;

    @NonNull
    @Schema(example = "d4bbd86c-27c0-4613-9f63-8f560aa47b85")
    UUID userIdentifier; // user-identifier

    @NonNull
    String token; // user-token

    @Schema(example = "view_workspace")
    List<@NonNull String> permission;

}