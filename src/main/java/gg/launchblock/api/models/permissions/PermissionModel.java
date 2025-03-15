package gg.launchblock.api.models.permissions;

import gg.launchblock.api.constants.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionModel {

    private String identifier;
    private String description;

    public static PermissionModel fromPermission(final Permission permission) {
        return new PermissionModel(permission.getIdentifier().toUpperCase(), permission.getDescription());
    }

}
