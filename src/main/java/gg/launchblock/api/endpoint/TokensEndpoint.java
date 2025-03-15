package gg.launchblock.api.endpoint;

import gg.launchblock.api.annotations.base.permissions.RequiredPermission;
import gg.launchblock.api.constants.Permission;
import gg.launchblock.api.exception.base.BuiltInExceptions;
import gg.launchblock.api.exception.base.LaunchBlockException;
import gg.launchblock.api.models.tokens.request.TokenCreateRequestModel;
import gg.launchblock.api.models.tokens.response.ExposedTokenResponseModel;
import gg.launchblock.api.models.tokens.response.TokenResponseModel;
import gg.launchblock.api.orchestrator.TokenOrchestrator;
import gg.launchblock.api.user.base.LaunchBlockActor;
import gg.launchblock.api.user.base.RequestContextHolder;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "API Tokens", description = "Management and metadata about API tokens")
@Path("/v1/tokens")
@RequiredArgsConstructor
public class TokensEndpoint {

    private final TokenOrchestrator orchestrator;
    private final RequestContextHolder contextHolder;

    @Operation(operationId = "Tokens.create", summary = "Create a new API token in an environment", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @RequiredPermission(value = Permission.CREATE_API_TOKENS)
    public Uni<ExposedTokenResponseModel> createToken(@Valid @NotNull final TokenCreateRequestModel body) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.orchestrator.createToken(
                this.contextHolder.getWorkspaceIdentifier().toString(),
                this.contextHolder.getEnvironmentIdentifier().toString(),
                body);
    }

    @Operation(operationId = "Tokens.list", summary = "Get a list of tokens within an environment", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @RequiredPermission(value = Permission.VIEW_API_TOKENS)
    public Uni<List<TokenResponseModel>> listTokens(@QueryParam("show_containers") @DefaultValue("false") final boolean showContainers) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.orchestrator.getTokens(
                this.contextHolder.getWorkspaceIdentifier().toString(),
                this.contextHolder.getEnvironmentIdentifier().toString(),
                showContainers);
    }

    @Operation(operationId = "Tokens.get", summary = "Get details about a specified API token")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.VIEW_API_TOKENS)
    public Uni<TokenResponseModel> getToken(
            @Schema(description = "The identifier of the token to view details of", examples = {"03e0db3a-3214-496b-b9c8-009dfda30453", "@self"})
            @DefaultValue("@self")
            @PathParam("identifier") final String tokenIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }

        final String workspaceId = this.contextHolder.getWorkspaceIdentifier().toString();
        final String environmentId = this.contextHolder.getEnvironmentIdentifier().toString();

        if ("@self".equals(tokenIdentifier)) {
            final LaunchBlockActor actor = this.contextHolder.getUser();
            if (actor.isUser()) {
                throw new LaunchBlockException(BuiltInExceptions.NO_PERMISSION, "Only API tokens can view this information!", "identifier");
            }
            return this.orchestrator.getToken(workspaceId, environmentId, actor.getIdentifier().toString());
        }

        return this.orchestrator.getToken(workspaceId, environmentId, tokenIdentifier);
    }

    @Operation(operationId = "Tokens.delete", summary = "Delete a specified API token", hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{identifier}")
    @RequiredPermission(value = Permission.DELETE_API_TOKENS)
    public Uni<Void> deleteToken(@NotNull @PathParam("identifier") final UUID tokenIdentifier) {
        if (this.contextHolder.getEnvironmentIdentifier() == null) {
            throw new LaunchBlockException(BuiltInExceptions.ESSENTIAL_HEADERS_MISSING, "environment-identifier");
        }
        return this.orchestrator.deleteToken(
                this.contextHolder.getWorkspaceIdentifier().toString(),
                this.contextHolder.getEnvironmentIdentifier().toString(),
                tokenIdentifier.toString());
    }

}