package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.environments.request.EnvironmentRequestModel;
import gg.launchblock.api.models.environments.response.EnvironmentResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "environments")
@Path("/environments")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface EnvironmentsClient {

    @POST
    Uni<EnvironmentResponseModel> createEnvironment(final EnvironmentRequestModel body);

    @GET
    Uni<List<EnvironmentResponseModel>> listEnvironments();

    @POST
    @Path("/{identifier}")
    Uni<EnvironmentResponseModel> updateEnvironment(@PathParam("identifier") UUID identifier, final EnvironmentRequestModel body);

    @DELETE
    @Path("/{identifier}")
    Uni<Void> deleteEnvironment(@PathParam("identifier") UUID identifier);


}
