package gg.launchblock.api.filter;


import gg.launchblock.api.constants.AuthConstants;
import gg.launchblock.api.user.base.RequestContextHolder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;


@RequestScoped
@Provider
@RequiredArgsConstructor
public class ForwardedHeadersFilter implements ClientHeadersFactory {

    @Inject
    RequestContextHolder requestContextHolder;

    @Override
    public MultivaluedMap<String, String> update(final MultivaluedMap<String, String> incomingHeaders, final MultivaluedMap<String, String> clientOutgoingHeaders) {
        if (this.requestContextHolder.getUser() == null) {
            return clientOutgoingHeaders;
        }

        final MultivaluedMap<String, String> result = new MultivaluedHashMap<>(clientOutgoingHeaders);

        result.add(AuthConstants.WORKSPACE_IDENTIFIER, this.requestContextHolder.getWorkspaceIdentifier().toString());
        result.add(AuthConstants.USER_IDENTIFIER, this.requestContextHolder.getUserIdentifier().toString());
        result.add(AuthConstants.USER_TOKEN, this.requestContextHolder.getUserToken());

        if (this.requestContextHolder.getEnvironmentIdentifier() != null) {
            result.add(AuthConstants.ENVIRONMENT_IDENTIFIER, this.requestContextHolder.getEnvironmentIdentifier().toString());
        }

        return result;
    }
}