package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.EnvironmentsClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.models.environments.request.EnvironmentRequestModel;
import gg.launchblock.api.models.environments.response.EnvironmentResponseModel;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Path("/v1/environments")
@RequiredArgsConstructor
public class EnvironmentsEndpoint {

    private final RequestContextHolder contextHolder;

    @RestClient
    @Inject
    EnvironmentsClient environmentsClient;

    @Operation(operationId = "Environments.create", summary = "Create an environment within a workspace", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @RequiredPermission(value = Permission.CREATE_ENVIRONMENTS)
    public Uni<EnvironmentResponseModel> createEnvironment(@Valid @NotNull final EnvironmentRequestModel body) {
        return this.environmentsClient.createEnvironment(body);
    }

    @Operation(operationId = "Environments.list", summary = "Get a list of environments within a workspace", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @RequiredPermission(value = Permission.VIEW_ENVIRONMENTS)
    public Uni<List<EnvironmentResponseModel>> listEnvironments() {
        return this.environmentsClient.listEnvironments();
    }

    @Operation(operationId = "Environments.update", summary = "Update the name of a specified environment", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.UPDATE_ENVIRONMENTS)
    public Uni<EnvironmentResponseModel> updateEnvironment(
            @NotNull @PathParam("identifier") final UUID identifier,
            @Valid @NotNull final EnvironmentRequestModel body) {
        return this.environmentsClient.updateEnvironment(identifier, body);
    }

    @Operation(operationId = "Environments.delete", summary = "Delete a specified environment from a workspace", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.DELETE_ENVIRONMENTS)
    public Uni<Void> deleteEnvironment(@NotNull @PathParam("identifier") final UUID identifier) {
        return this.environmentsClient.deleteEnvironment(identifier);
    }

}