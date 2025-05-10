package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.ScalingClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.scaling.ScalingGoalResponseModel;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.UUID;

@Tag(name = "Scaling", description = "View how we're scaling containers for projects")
@Path("/v1/projects/{project_identifier}/lifecycles/{deployment_identifier}/scaling")
@RequiredArgsConstructor
public class ScalingEndpoint {

    private final RequestContextHolder contextHolder;

    @RestClient
    @Inject
    ScalingClient scalingClient;

    @Operation(operationId = "Scaling.get",
            summary = "Get information about progress towards the set down scaling parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/")
    @RequiredPermission(value = Permission.VIEW_SCALE_GOALS)
    public Uni<ScalingGoalResponseModel> getScaleGoal(
            @NotNull @PathParam("deployment_identifier") final UUID deploymentIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.scalingClient.getScalingGoal(deploymentIdentifier);
    }

}