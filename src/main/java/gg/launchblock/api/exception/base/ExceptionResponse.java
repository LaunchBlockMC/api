package gg.launchblock.api.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ExceptionResponse {
    private String title;
    private ErrorDetails details;
    private String trace;
}