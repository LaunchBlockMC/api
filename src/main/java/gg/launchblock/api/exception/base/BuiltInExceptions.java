package gg.launchblock.api.exception.base;

import gg.launchblock.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuiltInExceptions implements ErrorCode {

    NO_PERMISSION("LB-1", 403, "No permission to do that", "user-token"),
    ESSENTIAL_HEADERS_MISSING("LB-2", 422, "Essential headers were missing", "user-token"),
    PRICING_FAILURE("LB-3", 500, "Error while logging API usage to our pricing system", "pricing"),
    OBJECT_NOT_FOUND("LB-NF", 404, "Cannot find that object with the given identifier", "object"),
    EXPIRED_INVALID_JWT("LB-JWT", 429, "Expired or invalid authentication provided", "user-token");

    final String errorCode;
    final int httpCode;
    final String message;
    final String field;

}