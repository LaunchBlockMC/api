```java
package gg.launchblock.template.endpoint;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Example API")
@Path("/example")
@AllArgsConstructor
public class ExampleAPI {


    private final UserOrchestrator userOrchestrator;

    @Operation(operationId = "User.onboard",
            summary = "Create a new user in a workspace")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/")
    public Uni<SafeAccountModel> onboardUser(
            @NotNull @HeaderParam(USER_IDENTIFIER) UUID userIdentifier,
            @NotNull @HeaderParam(WORKSPACE_IDENTIFIER) UUID workspaceIdentifier,
            @NotNull @HeaderParam(USER_TOKEN) String userToken,
            @NotNull @QueryParam("group_identifier") UUID group,
            final UserCreationRequest body) {
        return userOrchestrator.createUser(workspaceIdentifier, group, body);
    }


}
```