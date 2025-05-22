package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.MatchmakerClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.models.matchmaker.request.MatchSubmitRequest;
import gg.launchblock.api.models.matchmaker.response.MatchCreateResponseModel;
import gg.launchblock.api.models.matchmaker.response.MatchDetailsResponse;
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

@Tag(name = "Matchmaker", description = "Queue players for a project and view created matches")
@Path("/v1/matches")
@RequiredArgsConstructor
public class MatchmakerEndpoint {

    @Inject
    @RestClient
    MatchmakerClient matchmakerClient;

    @POST
    @Path("/")
    @Operation(operationId = "Matchmaker.create", summary = "Queue a player for a project", description = """
            Note: The project must belong to your workspace, otherwise you will not be able to queue players.
            """)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiredPermission(value = Permission.CREATE_MATCHMAKER_MATCH, containerOnly = true)
    public Uni<MatchCreateResponseModel> submitMatch(@Valid @NotNull final MatchSubmitRequest request) {
        // todo - verify project belongs to the token's workspace with redis
        // todo - fetch playercount details from definition (via redis)
        return this.matchmakerClient.createMatch(request);
    }

    @GET
    @Path("/{identifier}")
    @Operation(operationId = "Matchmaker.group", summary = "Get the information about a matchmaker group")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiredPermission(value = Permission.VIEW_MATCHMAKER_MATCH, containerOnly = true)
    public Uni<MatchDetailsResponse> getMatch(@NotNull final UUID identifier) {
        return this.matchmakerClient.getMatchGroupDetails(identifier);
    }

    @DELETE
    @Path("/{identifier}/players")
    @Operation(operationId = "Matchmaker.group", summary = "Remove players from a matchmaker match")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiredPermission(value = Permission.MANAGE_MATCHMAKER_MATCH, containerOnly = true)
    public Uni<Void> removePlayers(@NotNull @PathParam("identifier") final UUID groupId, @NotNull final List<UUID> playerIds) {
        // todo - possibly verify that the group belongs to this project
        return this.matchmakerClient.removePlayersFromGroup(groupId, playerIds);
    }

    // todo - move a player currently inside self container to another container manually

    // todo - is player queued for project?

}
