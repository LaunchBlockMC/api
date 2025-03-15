package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.lifecycles.response.LifecycleConfigurationResponseModel;
import gg.launchblock.api.models.lifecycles.response.LifecycleResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "lifecycles")
@Path("/lifecycle")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface LifecyclesClient {

    @GET
    @Path("/")
    Uni<List<LifecycleResponseModel>> listLifecycles(@QueryParam("limit") final int limit, @QueryParam("project_identifier") final UUID projectIdentifier);

    @GET
    @Path("/{identifier}")
    Uni<LifecycleResponseModel> getLifecycle(@PathParam("identifier") final UUID identifier);

    @GET
    @Path("/{identifier}/definition")
    Uni<LifecycleConfigurationResponseModel> getLifecycleDefinition(@NotNull @PathParam("identifier") final UUID identifier);

    @DELETE
    @Path("/{identifier}")
    Uni<LifecycleResponseModel> rollbackLifecycle(@PathParam("identifier") final UUID identifier);

}
