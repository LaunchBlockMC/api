package gg.launchblock.template.exception.base;

import gg.launchblock.template.exception.ErrorCode;
import io.quarkus.logging.Log;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

@RequiredArgsConstructor
@Getter
@AllArgsConstructor
public class LaunchBlockException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6243577914660599309L;
    private final String errorCode;
    private final int httpStatus;
    private final String message;
    private final Throwable originalException;
    private ErrorDetails errorDetails;

    public LaunchBlockException(final ErrorCode exception) {
        Log.debug("Error was thrown " + exception);
        this.errorCode = exception.getErrorCode();
        this.httpStatus = exception.getHttpCode();
        this.message = exception.getMessage();
        this.originalException = null;
        this.errorDetails = new ErrorDetails(this.errorCode, this.message, exception.getField(), "template");
    }

    public LaunchBlockException(final ErrorCode exception, final String field) {
        Log.debug("Error was thrown " + exception);
        this.errorCode = exception.getErrorCode();
        this.httpStatus = exception.getHttpCode();
        this.message = exception.getMessage();
        this.originalException = null;
        this.errorDetails = new ErrorDetails(this.errorCode, this.message, field, "template");
    }

    public LaunchBlockException(final ErrorCode exception, final String message, final String field) {
        Log.debug("Error was thrown " + exception);
        this.errorCode = exception.getErrorCode();
        this.httpStatus = exception.getHttpCode();
        this.message = message;
        this.originalException = null;
        this.errorDetails = new ErrorDetails(this.errorCode, message, field, "template");
    }

    public LaunchBlockException(final ErrorCode exception, final Throwable throwable) {
        Log.warn("Error was thrown " + throwable.getMessage());
        this.errorCode = exception.getErrorCode();
        this.httpStatus = exception.getHttpCode();
        this.message = exception.getMessage();
        this.originalException = throwable;
        this.errorDetails = new ErrorDetails(this.errorCode, this.message, exception.getField(), "template");
    }

}