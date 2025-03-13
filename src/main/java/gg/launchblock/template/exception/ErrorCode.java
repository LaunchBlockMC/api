package gg.launchblock.template.exception;

public interface ErrorCode {

    String getErrorCode();

    int getHttpCode();

    String getMessage();

    String getField();

}