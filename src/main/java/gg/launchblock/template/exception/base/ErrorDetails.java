package gg.launchblock.template.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private String errorCode;
    @Nullable
    private String message;
    @Nullable
    private String field;
    private String application;

}