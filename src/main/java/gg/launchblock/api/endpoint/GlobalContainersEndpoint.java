package gg.launchblock.api.endpoint;

import gg.launchblock.api.clients.base.ContainersClient;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/v1/containers")
@RequiredArgsConstructor
public class GlobalContainersEndpoint {

    @RestClient
    @Inject
    ContainersClient containersClient;

    @Operation(operationId = "Containers.global", summary = "Get the amount of containers running for all customers", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/")
    @CacheResult(cacheName = "containers_running")
    public Uni<Integer> getContainerSize() {
        return this.containersClient.getGlobalAmount();
    }


}