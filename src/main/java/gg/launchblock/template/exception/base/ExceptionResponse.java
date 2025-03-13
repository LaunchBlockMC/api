package gg.launchblock.template.exception.base;

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