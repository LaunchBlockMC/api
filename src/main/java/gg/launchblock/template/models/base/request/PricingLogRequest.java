package gg.launchblock.template.models.base.request;

import lombok.Data;
import lombok.Value;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Data
@Value
public class PricingLogRequest {

    @Schema(example = "post_created:tiktok", description = "The identifier of the unique pricing index")
    String pricingIdentifier;

    @Schema(example = "8703f8cf-b9c6-41da-b6ed-42fd0d4cc668")
    UUID userIdentifier;

    @Schema(example = "87fa68b7-f0dd-4bcf-a05b-080c67dfca57")
    UUID workspaceIdentifier;
}