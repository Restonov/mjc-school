package com.epam.esm.config;

import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * Handles exceptions that has been thrown in Controller
 */
@ControllerAdvice
public class AppExceptionHandler extends DefaultHandlerExceptionResolver {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String certificateNotFound(ResourceNotFoundException e) {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP Status: 404").append("\n")
                .append("response body").append("\n")
                .append("{").append("\n")
                .append("\"error message\" : ").append(e.getMessage()).append("\n")
                .append("}");
        return builder.toString();
    }
}
