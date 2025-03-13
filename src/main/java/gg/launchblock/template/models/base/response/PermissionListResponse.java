package gg.launchblock.template.models.base.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Data
public class PermissionListResponse {

    @NonNull
    UUID workspaceIdentifier;
    @NonNull
    UUID userIdentifier; // user-identifier
    @NonNull
    Set<@NonNull String> permissions;
    int highestGroupPriority;

    @JsonIgnore
    public boolean hasPermission(final String permission) {
        return this.permissions.contains(permission);
    }

    @JsonIgnore
    public boolean hasPermissions(final Set<String> permission) {
        return this.permissions.containsAll(permission);
    }

}