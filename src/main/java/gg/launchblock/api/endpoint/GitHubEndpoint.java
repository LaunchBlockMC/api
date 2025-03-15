package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.clients.base.GithubClient;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.AppExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.github.response.GitCommitResponseModel;
import gg.launchblock.api.models.github.response.GitRepositoryResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Set;

@Path("/v1/github")
@Tag(name = "GitHub Metadata", description = "GitHub metadata for repositories linked to the workspace")
@RequiredArgsConstructor
public class GitHubEndpoint {

    @RestClient
    @Inject
    GithubClient githubClient;

    @Operation(operationId = "GitHub.generateInstallationToken", summary = "Generate an installation token for the github app", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @GET
    @Path("/install")
    @RequiredPermission(value = Permission.UPDATE_GITHUB_CONNECTION)
    public Uni<String> generateInstallToken() {
        return this.githubClient.generateInstallToken();
    }

    @Operation(operationId = "GitHub.listRepositories", summary = "Get a list of repositories in a workspace")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/repositories")
    @RequiredPermission(value = Permission.VIEW_GITHUB_REPOSITORIES)
    public Uni<List<GitRepositoryResponseModel>> listRepositories() {
        return this.githubClient.listRepositories();
    }

    @Operation(operationId = "GitHub.listBranches", summary = "Get a list of branches on a repository")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/branches")
    @RequiredPermission(value = Permission.VIEW_GITHUB_BRANCHES)
    public Uni<Set<String>> listBranches(
            @Schema(description = "A link to the GitHub repository", example = "https://github.com/LaunchBlockMC/example-skywars")
            @NotNull @QueryParam("repository_url") final String repository) {
        if (!repository.toLowerCase().contains("https://github.com")) {
            throw new LaunchBlockException(AppExceptions.INVALID_PARAMETER, "repository_url");
        }
        return this.githubClient.listBranches(repository);
    }

    @Operation(operationId = "GitHub.listCommits", summary = "Get a list of commits on a branch of a repository")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/commits")
    @RequiredPermission(value = Permission.VIEW_GITHUB_COMMITS)
    public Uni<List<GitCommitResponseModel>> listCommits(
            @Schema(description = "A link to the GitHub repository", example = "https://github.com/LaunchBlockMC/example-skywars")
            @NotNull @QueryParam("repository_url") final String repository,
            @Schema(description = "Name of the branch to find commits on", example = "main")
            @NotNull @QueryParam("branch_name") final String branch) {
        if (!repository.toLowerCase().contains("https://github.com")) {
            throw new LaunchBlockException(AppExceptions.INVALID_PARAMETER, "repository_url");
        }
        return this.githubClient.listCommits(repository, branch);
    }

}