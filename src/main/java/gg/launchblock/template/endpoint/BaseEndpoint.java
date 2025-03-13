package gg.launchblock.template.endpoint;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/")
public class BaseEndpoint {

    @ConfigProperty(name = "base-url", defaultValue = "https://launchblock.gg")
    String baseUrl;

    @Operation(hidden = true)
    @GET
    public Uni<Response> redirect() {
        return Uni.createFrom().item(Response.seeOther(UriBuilder.fromUri(this.baseUrl).build()).build());
    }

}