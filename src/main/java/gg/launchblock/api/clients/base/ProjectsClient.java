package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.projects.request.ProjectCreateUpdateRequestModel;
import gg.launchblock.api.models.projects.response.ProjectResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "projects")
@Path("/projects")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface ProjectsClient {

    @POST
    Uni<ProjectResponseModel> createProject(final ProjectCreateUpdateRequestModel body);

    @GET
    Uni<List<ProjectResponseModel>> listProjects();

    @GET
    @Path("/{identifier}")
    Uni<ProjectResponseModel> getProject(@PathParam("identifier") final UUID identifier);

    @POST
    @Path("/{identifier}")
    Uni<ProjectResponseModel> updateProject(@PathParam("identifier") final UUID identifier, final ProjectCreateUpdateRequestModel body);

    @DELETE
    @Path("/{identifier}")
    Uni<Void> deleteProject(@PathParam("identifier") final UUID identifier);

}
