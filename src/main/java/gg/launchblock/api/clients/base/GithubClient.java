package gg.launchblock.api.clients.base;

import gg.launchblock.api.filter.ForwardedHeadersFilter;
import gg.launchblock.api.models.github.response.GitCommitResponseModel;
import gg.launchblock.api.models.github.response.GitRepositoryResponseModel;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Set;

@RegisterRestClient(configKey = "github")
@RegisterClientHeaders(ForwardedHeadersFilter.class)
public interface GithubClient {

    @GET
    @Path("/branches")
    Uni<Set<String>> listBranches(@QueryParam("repository") final String repository);

    @GET
    @Path("/commits")
    Uni<List<GitCommitResponseModel>> listCommits(@QueryParam("repository") final String repository, @QueryParam("branch") final String branch);

    @GET
    @Path("/repos")
    Uni<List<GitRepositoryResponseModel>> listRepositories();

    @GET
    @Path("/install")
    Uni<String> generateInstallToken();

}
