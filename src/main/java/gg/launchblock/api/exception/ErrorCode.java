package gg.launchblock.api.exception;

public interface ErrorCode {

    String getErrorCode();

    int getHttpCode();

    String getMessage();

    String getField();

}