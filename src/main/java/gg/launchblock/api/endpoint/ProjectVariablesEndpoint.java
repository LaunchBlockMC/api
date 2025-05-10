package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.VariablesClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.variables.request.VariableCreateRequestModel;
import gg.launchblock.api.models.variables.response.VariableResponseModel;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Tag(name = "Project Variables", description = "Manage environment variables for a project")
@Path("/v1/projects/{project_identifier}")
@RequiredArgsConstructor
public class ProjectVariablesEndpoint {

    private final RequestContextHolder contextHolder;

    @RestClient
    @Inject
    VariablesClient variablesClient;

    @Operation(operationId = "ProjectVariables.create", summary = "Create a list of variables within a project")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/variables")
    @RequiredPermission(value = Permission.CREATE_VARIABLES)
    public Uni<List<VariableResponseModel>> createVariables(
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier,
            @NotNull final List<@NotEmpty @NotNull VariableCreateRequestModel> body) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.variablesClient.createVariables(projectIdentifier, body);
    }

    @Operation(operationId = "ProjectVariables.delete", summary = "Delete variables from a project")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/variables")
    @RequiredPermission(value = Permission.DELETE_VARIABLES)
    public Uni<Void> deleteVariables(
            @NotNull @PathParam("project_identifier") final UUID projectIdentifier,
            @NotNull final List<@NotEmpty @NotNull UUID> identifiers) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.variablesClient.deleteVariables(identifiers);
    }

}