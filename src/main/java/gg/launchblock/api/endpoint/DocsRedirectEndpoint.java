package gg.launchblock.api.endpoint;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/")
public class DocsRedirectEndpoint {

    @ConfigProperty(name = "base-url", defaultValue = "https://docs.launchblock.gg/docs/api")
    String baseUrl;

    @Operation(hidden = true)
    @GET
    public Uni<Response> redirect() {
        return this.redirectDocs();
    }

    @Operation(hidden = true)
    @GET
    @Path("/docs")
    public Uni<Response> redirectDocs() {
        return Uni.createFrom().item(Response.seeOther(UriBuilder.fromUri(this.baseUrl).build()).build());
    }

}