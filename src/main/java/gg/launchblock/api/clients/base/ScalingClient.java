package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.scaling.ScalingGoalResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.UUID;

@RegisterRestClient(configKey = "scaling")
@Path("/scale_goals")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface ScalingClient {

    @GET
    @Path("/{identifier}")
    Uni<ScalingGoalResponseModel> getScalingGoal(@PathParam("identifier") final UUID identifier);

}
