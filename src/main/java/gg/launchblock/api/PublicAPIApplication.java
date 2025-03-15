package gg.launchblock.api;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@SecurityScheme(
        securitySchemeName = "Authorization",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        apiKeyName = "Authorization",
        description = """
                This API requires authentication to use! This helps us understand identities, permissions and auditing!
                
                To generate a Token for local usage, go to your canvas, click into an environment and click Settings -> Tokens -> New Token.
                
                If you're using this API inside of a container, this is automatically generated in the `LB_API_TOKEN` environment variable.
                """
)
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "https://api.launchblock.gg",
                        description = "For usage on local development setups, reference this."),
                @Server(
                        url = "http://api-public.launchblock-services.svc.cluster.local",
                        description = "For usage inside of LaunchBlock containers, reference this."
                )
        },
        security = {@SecurityRequirement(name = "Authorization")},
        info =
        @Info(
                title = "Public LaunchBlock API",
                version = "0.0.1",
                contact = @Contact(name = "LaunchBlock Discord Server", url = "https://discord.gg/LaunchBlock")),
        externalDocs =
        @ExternalDocumentation(
                description =
                        """
                                View the LaunchBlock API documentation, tutorials and more!
                                """,
                url = "https://api.launchblock.gg/docs"))
public class PublicAPIApplication extends Application {
}

