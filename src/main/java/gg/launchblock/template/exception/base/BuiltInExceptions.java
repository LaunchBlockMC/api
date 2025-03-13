package gg.launchblock.template.exception.base;

import gg.launchblock.template.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuiltInExceptions implements ErrorCode {

    NO_PERMISSION("PS-1", 403, "No permission to do that", "user-token"),
    ESSENTIAL_HEADERS_MISSING("PS-2", 422, "Essential headers were missing", "user-token"),
    PRICING_FAILURE("PS-3", 500, "Error while logging API usage to our pricing system", "pricing"),
    OBJECT_NOT_FOUND("PS-NF", 404, "Cannot find that object with the given identifier", "object"),
    EXPIRED_INVALID_JWT("PS-JWT", 429, "Expired or invalid authentication provided", "user-token");

    final String errorCode;
    final int httpCode;
    final String message;
    final String field;

}