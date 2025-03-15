package gg.launchblock.api.models.lifecycles;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class ProjectVolumesModel {

    private String name;
    private String mountPath;

    @Schema(format = "\\b\\d+(?:\\.\\d+)?\\s*(?:B|Ki|Mi|Gi|Ti|Pi|Ei|Zi|Yi)\\b\n", example = "10Gi")
    private String size;

}
