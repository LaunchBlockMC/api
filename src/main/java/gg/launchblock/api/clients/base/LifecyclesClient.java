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
    Uni<List<LifecycleResponseModel>> listLifecycles(@QueryParam("project_identifier") final UUID projectIdentifier, @QueryParam("per_page") final int perPage, @QueryParam("page") final int page);

    @GET
    @Path("/{identifier}")
    Uni<LifecycleResponseModel> getLifecycle(@PathParam("identifier") final UUID identifier);

    @GET
    @Path("/{identifier}/definition")
    Uni<LifecycleConfigurationResponseModel> getLifecycleDefinition(@NotNull @PathParam("identifier") final UUID identifier);

    @DELETE
    @Path("/{identifier}")
    Uni<Void> deleteLifecycle(@PathParam("identifier") final UUID identifier, @QueryParam("force") @DefaultValue("false") final boolean force);

}
