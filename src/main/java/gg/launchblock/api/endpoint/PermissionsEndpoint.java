package gg.launchblock.api.endpoint;

import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.models.permissions.PermissionModel;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Permissions", description = "Permissions that can be awarded to an API token")
@Path("/v1/permissions")
@RequiredArgsConstructor
public class PermissionsEndpoint {

    @Operation(operationId = "Permissions.staticList",
            summary = "Get a static list of permissions you can add to an API token")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/static")
    public Uni<Set<PermissionModel>> listStaticPermissions() {
        return Uni.createFrom().item(
                Arrays.stream(Permission.values())
                        .filter(Permission::isCanAddToToken)
                        .map(PermissionModel::fromPermission)
                        .collect(Collectors.toSet()));
    }

}