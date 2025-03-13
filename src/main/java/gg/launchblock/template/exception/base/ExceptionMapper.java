package gg.launchblock.template.exception.base;

import io.opentelemetry.api.trace.Span;
import io.quarkus.logging.Log;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Provider
public class ExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Exception> {

    public static String getCurrentTraceId() {
        final Span currentSpan = Span.current();
        return currentSpan.getSpanContext().getTraceId();
    }

    @Override
    public Response toResponse(final Exception e) {
        if (e instanceof final LaunchBlockException launchBlockException) {
            return Response.status(launchBlockException.getHttpStatus())
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ExceptionResponse("There was an error in your request", launchBlockException.getErrorDetails(), ExceptionMapper.getCurrentTraceId()))
                    .build();
        }

        if (e instanceof final NotAllowedException notAllowedException) {
            return Response.status(405)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ExceptionResponse("Method not allowed", new ErrorDetails("?", "Perhaps you're trying to do the wrong http method?", "", "template"), ExceptionMapper.getCurrentTraceId()))
                    .build();
        }

        if (e instanceof final ClientWebApplicationException clientWebApplicationException) {
            final Response response = clientWebApplicationException.getResponse();
            final ExceptionResponse exceptionResponse = response.readEntity(ExceptionResponse.class);
            final ErrorDetails errorDetails = exceptionResponse.getDetails();
            errorDetails.setMessage(errorDetails.getMessage() + " " + "(fwd)");

            return Response.status(response.getStatus())
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(exceptionResponse)
                    .build();

        }

        if (e instanceof final NotFoundException notFoundException) {
            return Response.status(404)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ExceptionResponse("Method not found", new ErrorDetails("PS-NF", "That method doesn't exist, try the documentation?", "", "template"), ExceptionMapper.getCurrentTraceId()))
                    .build();
        }

        Log.error("Unhandled error occurred " + e.getMessage() + " " + e.getClass().getSimpleName());
        return Response.status(500)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ExceptionResponse("An unexpected error happened while processing this request", new ErrorDetails("?", "An unhandled error occurred", "", "template"), ExceptionMapper.getCurrentTraceId()))
                .build();

    }
}