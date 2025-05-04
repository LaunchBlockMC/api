package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.ContainersClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.container.response.ContainerResponseModel;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Tag(name = "Containers", description = "View and manage containers running on a deployment within a project")
@Path("/v1/projects/{project_identifier}/lifecycles/{deployment_identifier}/containers")
@RequiredArgsConstructor
public class ContainersEndpoint {

    private final RequestContextHolder contextHolder;

    @RestClient
    @Inject
    ContainersClient containersClient;

    @Operation(operationId = "Containers.create", summary = "Summon a new container attached to a deployment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/")
    @RequiredPermission(value = Permission.CREATE_CONTAINERS)
    public Uni<ContainerResponseModel> createContainer(@NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.containersClient.createContainer(deploymentIdentifier);
    }

    @Operation(operationId = "Containers.list", summary = "Get a list of running containers in a deployment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/")
    @RequiredPermission(value = Permission.VIEW_CONTAINERS)
    public Uni<List<ContainerResponseModel>> listContainers(
            @NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier,
            @QueryParam("healthy_only") @DefaultValue("false") final boolean healthyOnly,
            @QueryParam("online_only") @DefaultValue("false") final boolean onlineOnly,
            @QueryParam("limit") @DefaultValue("100") final int limit) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.containersClient.getContainers(deploymentIdentifier, healthyOnly, onlineOnly, limit);
    }

    @Operation(operationId = "Containers.delete", summary = "Remove a specified container that is attached to a deployment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.CREATE_CONTAINERS)
    public Uni<ContainerResponseModel> deleteContainer(
            @NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier,
            @NotNull @PathParam("identifier") final UUID containerIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.containersClient.deleteContainer(deploymentIdentifier, containerIdentifier);
    }


}