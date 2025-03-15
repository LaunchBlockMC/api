package gg.launchblock.api.user.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@RequiredArgsConstructor
public class LaunchBlockActor {

    private final static String WILCARD = "*";

    private final boolean isUser;
    private final UUID identifier;

    // workspaceid : permissions
    @Getter(AccessLevel.PRIVATE)
    private Map<String, Set<String>> permissions = new HashMap<>();
    private Map<String, Integer> highestGroupPriority = new HashMap<>();
    private Map<String, Object> extraObjects = new HashMap<>();

    private static boolean matchesWildcard(final String pattern, final String permission) {
        if (pattern.equals(LaunchBlockActor.WILCARD) && !permission.equals("application")) {
            return true;
        }

        if (pattern.endsWith(LaunchBlockActor.WILCARD) && !permission.equals("application")) {
            final String prefix = pattern.substring(0, pattern.length() - 1);
            return permission.startsWith(prefix);
        }

        // For exact match
        return pattern.equals(permission);
    }

    @JsonCreator
    public static LaunchBlockActor fromUri(final String uri) {
        final boolean isUser = uri.startsWith("user:");
        final UUID uuid = UUID.fromString(uri.split(":")[1]);
        return new LaunchBlockActor(isUser, uuid);
    }

    public int getHighestGroupPriority(final String workspaceUUID) {
        return this.highestGroupPriority.get(workspaceUUID);
    }

    public void setHighestGroupPriority(final String workspaceUUID, final int priority) {
        this.highestGroupPriority.put(workspaceUUID, priority);
    }

    @Contract(mutates = "this")
    public LaunchBlockActor setPermissions(final String workspaceUUID, final Set<String> permissions) {
        this.permissions.put(workspaceUUID, permissions);
        return this;
    }

    public boolean hasPermission(final String workspaceID, final String permission) {
        if (!this.permissions.containsKey(workspaceID)) {
            return false;
        }

        final Set<String> workspacePermissions = this.permissions.get(workspaceID);
        return workspacePermissions.stream().anyMatch(p -> LaunchBlockActor.matchesWildcard(p, permission));
    }

    public boolean hasPermissions(final String workspaceId, final Set<String> check) {
        if (!this.permissions.containsKey(workspaceId)) {
            return false;
        }

        final Set<String> workspacePermissions = this.permissions.get(workspaceId);
        return check.stream().allMatch(permission ->
                workspacePermissions.stream().anyMatch(p -> LaunchBlockActor.matchesWildcard(p, permission))
        );
    }

    @JsonValue
    public String getUri() {
        return (this.isUser ? "user:" : "application:") + this.identifier;
    }

}