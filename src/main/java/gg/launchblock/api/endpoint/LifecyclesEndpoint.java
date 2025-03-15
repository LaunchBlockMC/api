package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.LifecyclesClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.lifecycles.response.LifecycleConfigurationResponseModel;
import gg.launchblock.api.models.lifecycles.response.LifecycleResponseModel;
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

@Tag(name = "Lifecycles", description = "Trigger and get information about builds & deployments for a project")
@Path("/v1/projects/{project_identifier}/lifecycles")
@RequiredArgsConstructor
public class LifecyclesEndpoint {

    private final RequestContextHolder contextHolder;

    @RestClient
    @Inject
    LifecyclesClient lifecyclesClient;

    @Operation(operationId = "Lifecycle.list", summary = "Get a list of lifecycles in a project")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @RequiredPermission(value = Permission.VIEW_LIFECYCLES)
    public Uni<List<LifecycleResponseModel>> listLifecycles(
            @QueryParam("limit") @DefaultValue("25") final int limit,
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.lifecyclesClient.listLifecycles(limit, projectIdentifier);
    }

    @Operation(operationId = "Lifecycle.get", summary = "Get a specified lifecycle in a project")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.VIEW_LIFECYCLES)
    public Uni<LifecycleResponseModel> getLifecyle(
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier,
            @NotNull @PathParam("identifier") final UUID identifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.lifecyclesClient.getLifecycle(identifier);
    }

    @Operation(operationId = "Lifecycle.configuration", summary = "Get the project configuration attached to a lifecycle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}/configuration")
    @RequiredPermission(value = Permission.VIEW_LIFECYCLES)
    public Uni<LifecycleConfigurationResponseModel> getLifecyleConfiguration(
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier,
            @NotNull @PathParam("identifier") final UUID identifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.lifecyclesClient.getLifecycleDefinition(identifier);
    }

    @Operation(operationId = "Lifecycle.rollback", summary = "Rollback a specified lifecycle in a project to the previous successful lifecycle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.ROLLBACK_LIFECYCLES)
    public Uni<LifecycleResponseModel> rollbackLifecycle(
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier,
            @NotNull @PathParam("identifier") final UUID identifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.lifecyclesClient.rollbackLifecycle(identifier);
    }

}