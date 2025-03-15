package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.ProjectsClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.projects.request.ProjectCreateUpdateRequestModel;
import gg.launchblock.api.models.projects.response.ProjectResponseModel;
import gg.launchblock.api.user.base.RequestContextHolder;
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

@Tag(name = "Projects", description = "Create and manage projects within an environment")
@Path("/v1/projects")
@RequiredArgsConstructor
public class ProjectsEndpoint {

    private final RequestContextHolder contextHolder;

    @RestClient
    @Inject
    ProjectsClient projectsClient;

    @Operation(operationId = "Projects.create", summary = "Create a project within an environment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @RequiredPermission(value = Permission.CREATE_PROJECTS)
    public Uni<ProjectResponseModel> createProject(@Valid @NotNull final ProjectCreateUpdateRequestModel body) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        if (body.getEnvironmentIdentifier() != this.contextHolder.getEnvironmentIdentifier()) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING,
                    "Environment identifier provided in body mismatches authorization headers", "environment-identifier");
        }
        return this.projectsClient.createProject(body);
    }

    @Operation(operationId = "Projects.list", summary = "Get a list of projects within an environment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @RequiredPermission(value = Permission.VIEW_WORKSPACE)
    public Uni<List<ProjectResponseModel>> listProjects() {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.projectsClient.listProjects();
    }

    @Operation(operationId = "Projects.get", summary = "Get a specified project within an environment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.VIEW_WORKSPACE)
    public Uni<ProjectResponseModel> getProject(@NotNull @PathParam("identifier") final UUID identifier) {
        return this.projectsClient.getProject(identifier);
    }

    @Operation(operationId = "Projects.update", summary = "Update the details about a project within an environment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.UPDATE_PROJECTS)
    public Uni<ProjectResponseModel> updateEnvironment(
            @NotNull @PathParam("identifier") final UUID identifier,
            @Valid @NotNull final ProjectCreateUpdateRequestModel body) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        if (body.getEnvironmentIdentifier() != this.contextHolder.getEnvironmentIdentifier()) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING,
                    "Environment identifier provided in body mismatches authorization headers", "environment-identifier");
        }
        return this.projectsClient.updateProject(identifier, body);
    }

    @Operation(operationId = "Projects.delete", summary = "Delete a specified project from an environment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.DELETE_PROJECTS)
    public Uni<Void> deleteEnvironment(@NotNull @PathParam("identifier") final UUID identifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.projectsClient.deleteProject(identifier);
    }

}