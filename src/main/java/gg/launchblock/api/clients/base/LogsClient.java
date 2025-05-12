package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.lifecycles.LifecycleStage;
import gg.launchblock.api.models.logs.LogLevel;
import gg.launchblock.api.models.logs.request.LogRequestModel;
import gg.launchblock.api.models.logs.response.LogResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "logs")
@Path("/logs")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface LogsClient {

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    Uni<List<LogResponseModel>> listLogs(
            @NotNull @QueryParam("stage") final LifecycleStage stage,
            @QueryParam("lifecycle_identifier") final UUID deploymentIdentifier,
            @QueryParam("container_identifier") final UUID containerIdentifier,
            @QueryParam("message") final String message,
            @QueryParam("log_level") final LogLevel logLevel,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("per_page") @DefaultValue("50") final int perPage);

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    Uni<List<LogResponseModel>> listLogs(
            @NotNull @QueryParam("stage") final LifecycleStage stage,
            @QueryParam("lifecycle_identifier") final UUID deploymentIdentifier,
            @QueryParam("message") final String message,
            @QueryParam("log_level") final LogLevel logLevel,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("per_page") @DefaultValue("50") final int perPage);

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/{lifecycle_identifier}")
    Uni<List<LogResponseModel>> createLogs(
            @NotNull @PathParam("lifecycle_identifier") final UUID lifecycleIdentifier,
            @NotNull final List<LogRequestModel> logModels);

}
