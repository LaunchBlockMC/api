package gg.launchblock.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppExceptions implements ErrorCode {

    INVALID_PARAMETER("LB-32", 422, "Invalid parameter provided", "field");

    final String errorCode;
    final int httpCode;
    final String message;
    final String field;

}