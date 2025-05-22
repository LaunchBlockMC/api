package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.LogsClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.models.logs.request.LogRequestModel;
import gg.launchblock.api.models.logs.response.LogResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Tag(name = "Logs", description = "Fetch and query logs being emitted from projects")
@Path("/v1/logs")
@RequiredArgsConstructor
public class LogsEndpoint {

    @Inject
    @RestClient
    LogsClient logsClient;

    @Operation(operationId = "Logs.create", summary = "Create a list of logs coming from a container", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/lifecycle/{lifecycle_identifier}")
    @RequiredPermission(value = Permission.CREATE_LOGS, containerOnly = true)
    public Uni<List<LogResponseModel>> createLogs(
            @NotNull @PathParam("lifecycle_identifier") final UUID lifecycleIdentifier,
            @Valid @NotNull final List<LogRequestModel> logModels) {
        return this.logsClient.createLogs(lifecycleIdentifier, logModels);
    }

}