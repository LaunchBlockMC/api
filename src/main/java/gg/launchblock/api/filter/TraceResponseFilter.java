package gg.launchblock.api.filter;


import gg.launchblock.api.user.base.RequestContextHolder;
import io.opentelemetry.api.trace.Span;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;


@RequestScoped
@Provider
@RequiredArgsConstructor
public class TraceResponseFilter {

    @Inject
    RequestContextHolder requestContextHolder;

    private static String getCurrentTraceId() {
        final Span currentSpan = Span.current();
        return currentSpan.getSpanContext().getTraceId();
    }

    @ServerResponseFilter(priority = 10000)
    public void getFilter(final ContainerResponseContext ctx) {
        ctx.getHeaders().add("trace", TraceResponseFilter.getCurrentTraceId());
    }
}