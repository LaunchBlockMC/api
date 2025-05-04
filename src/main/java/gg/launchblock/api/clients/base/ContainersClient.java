package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.container.response.ContainerResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "containers")
@Path("/containers")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface ContainersClient {

    @GET
    @Path("/")
    Uni<Integer> getGlobalAmount();

    @GET
    @Path("/{deployment_identifier}")
    Uni<List<ContainerResponseModel>> getContainers(
            @NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier,
            @QueryParam("healthy_only") @DefaultValue("false") final boolean healthyOnly,
            @QueryParam("online_only") @DefaultValue("false") final boolean onlineOnly,
            @QueryParam("limit") @DefaultValue("100") final int limit);

    @POST
    @Path("/{deployment_identifier}")
    Uni<ContainerResponseModel> createContainer(@NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier);

    @DELETE
    @Path("/{deployment_identifier}/{container_identifier}")
    Uni<ContainerResponseModel> deleteContainer(
            @NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier,
            @NotNull @PathParam("container_identifier") final UUID containerIdentifier);

}
