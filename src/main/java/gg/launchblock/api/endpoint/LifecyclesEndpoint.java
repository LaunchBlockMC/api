package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.LifecyclesClient;
import gg.launchblock.api.clients.base.LogsClient;
import gg.launchblock.api.clients.base.VariablesClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.lifecycles.LifecycleStage;
import gg.launchblock.api.models.lifecycles.response.LifecycleConfigurationResponseModel;
import gg.launchblock.api.models.lifecycles.response.LifecycleResponseModel;
import gg.launchblock.api.models.logs.LogLevel;
import gg.launchblock.api.models.logs.response.LogResponseModel;
import gg.launchblock.api.models.variables.response.VariableResponseModel;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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

    @RestClient
    @Inject
    LogsClient logsClient;

    @RestClient
    @Inject
    VariablesClient variablesClient;

    @Operation(operationId = "Lifecycle.list", summary = "Get a list of lifecycles in a project")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @RequiredPermission(value = Permission.VIEW_LIFECYCLES)
    public Uni<List<LifecycleResponseModel>> listLifecycles(
            @QueryParam("per_page") @DefaultValue("25") @Min(1) @Max(200) final int perPage,
            @QueryParam("page") @DefaultValue("1") @Min(value = 1) final int page,
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.lifecyclesClient.listLifecycles(projectIdentifier, perPage, page);
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

    @Operation(operationId = "Lifecycle.rollback", summary = "Rollback to a previous lifecycle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/")
    @RequiredPermission(value = Permission.ROLLBACK_LIFECYCLES)
    public Uni<LifecycleResponseModel> rollbackLifecycle(
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier,
            @QueryParam("target_lifecycle_identifier") @Schema(description = "The lifecycle identifier to rollback to") final UUID identifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return Uni.createFrom().nullItem(); // todo
    }

    @Operation(operationId = "Lifecycle.delete", summary = "Delete a lifecycle by its identifier")
    @DELETE
    @Path("/{identifier}")
    @RequiredPermission(Permission.REMOVE_LIFECYCLES)
    public Uni<Void> deleteLifecycle(
            @NotNull @PathParam("identifier") final UUID identifier,
            @Schema(description = "Force the deletion instantly. If true, the deletion will immediately remove the associated resources (not graceful).")
            @QueryParam("force") @DefaultValue("false") final boolean force) {
        return this.lifecyclesClient.deleteLifecycle(identifier, force);
    }

    @Operation(operationId = "Logs.list", summary = "Get a list of logs attached to a lifecycle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}/logs")
    @RequiredPermission(value = Permission.VIEW_LOGS)
    public Uni<List<LogResponseModel>> listLogs(
            @NotNull @PathParam("identifier") final UUID lifecycleIdentifier,
            @QueryParam("lifecycle_stage") @DefaultValue("DEPLOY") final LifecycleStage lifecycleStage,
            @QueryParam("message") @Schema(description = "Find logs where this is included in the message field") final String message,
            @QueryParam("log_level") final LogLevel logLevel,
            @QueryParam("page") @DefaultValue("1") @Min(value = 1) final int page,
            @QueryParam("per_page") @DefaultValue("50") @Min(value = 1) @Max(value = 100) final int perPage) {
        return this.logsClient.listLogs(lifecycleStage, lifecycleIdentifier, message, logLevel, page, perPage);
    }

    @Operation(operationId = "ProjectVariables.list", summary = "List all variables in a deployed version of the project")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}/variables")
    @RequiredPermission(value = Permission.VIEW_VARIABLES)
    public Uni<List<VariableResponseModel>> listVariables(@NotNull @PathParam("identifier") final UUID lifecycleIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.variablesClient.listVariablesLifecycle(lifecycleIdentifier);
    }

    @Operation(operationId = "ProjectVariables.get", summary = "Get a project's variable by the variable's identifier")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}/variables/{variable_identifier}")
    @RequiredPermission(value = Permission.VIEW_VARIABLES)
    public Uni<VariableResponseModel> getVariable(
            @NotNull @PathParam("identifier") final UUID lifecycleIdentifier,
            @NotNull @PathParam("variable_identifier") final UUID identifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.variablesClient.getVariable(identifier);
    }
}