package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.matchmaker.request.MatchSubmitRequest;
import gg.launchblock.api.models.matchmaker.response.MatchCreateResponseModel;
import gg.launchblock.api.models.matchmaker.response.MatchDetailsResponse;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "matchmaker")
@Path("/match_groups")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface MatchmakerClient {

    @POST
    @Path("/generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<MatchCreateResponseModel> createMatch(final MatchSubmitRequest request);

    @GET
    @Path("/{group_identifier}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<MatchDetailsResponse> getMatchGroupDetails(@NotNull @PathParam("group_identifier") final UUID groupId);

    @DELETE
    @Path("/{group_identifier}/players")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Void> removePlayersFromGroup(@NotNull @PathParam("group_identifier") final UUID groupId, @NotNull final List<UUID> playerIds);

}
