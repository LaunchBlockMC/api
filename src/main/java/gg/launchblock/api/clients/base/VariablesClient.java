package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.variables.request.VariableCreateRequestModel;
import gg.launchblock.api.models.variables.request.VariableUpdateRequestModel;
import gg.launchblock.api.models.variables.response.VariableResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "variables")
@Path("/variables")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface VariablesClient {

    @POST
    @Path("/")
    Uni<List<VariableResponseModel>> createVariables(
            @NotNull @QueryParam("project_identifier") final UUID projectIdentifier,
            @NotNull final List<@NotEmpty VariableCreateRequestModel> body);

    @GET
    @Path("/")
    Uni<List<VariableResponseModel>> listVariablesLifecycle(@QueryParam("lifecycle_identifier") final UUID lifecycleIdentifier);

    @GET
    @Path("/")
    Uni<List<VariableResponseModel>> listVariablesEnvironment(@QueryParam("environment_identifier") final UUID environmentIdentifier);

    @GET
    @Path("/{identifier}")
    Uni<VariableResponseModel> getVariable(@NotNull @PathParam("identifier") final UUID identifier);

    @PUT
    @Path("/")
    Uni<List<VariableResponseModel>> updateVariables(@NotNull final List<@NotEmpty VariableUpdateRequestModel> body);

    @DELETE
    @Path("/")
    Uni<Void> deleteVariables(@NotNull final List<@NotEmpty UUID> identifiers);

}
