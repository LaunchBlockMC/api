package gg.launchblock.template;

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
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;

@SecurityScheme(
        securitySchemeName = "user-token",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        apiKeyName = "user-token",
        description = "Token for user authentication"
)
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "https://template.launchblock.{tld}",
                        variables = {
                                @ServerVariable(name = "tld", defaultValue = "com"), // xyz for pre-prod
                        })
        },
        security = {@SecurityRequirement(name = "user-token")},
        info =
        @Info(
                title = "Template",
                version = "1.0.0-SNAPSHOT",
                contact = @Contact(name = "Customer Support", email = "support@launchblock.gg"),
                description =
                        """
                                Template description
                                """),
        externalDocs =
        @ExternalDocumentation(
                description =
                        """
                                Template description
                                """,
                url = "https://<OpenIPALocation>"))
public class TemplateApplication extends Application {
}

