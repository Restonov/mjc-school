package com.epam.esm.controller;

import com.epam.esm.dto.ExceptionDto;
import com.epam.esm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Objects;

/**
 * Handles exceptions that has been thrown in Controller
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends DefaultHandlerExceptionResolver {
    private static final int NOT_FOUND_CUSTOM_CODE = 40401;
    private static final int BAD_REQUEST_CUSTOM_CODE = 40005;
    private static final int NOT_ACCEPTABLE_CUSTOM_CODE = 40006;
    private static final int VALIDATION_CUSTOM_CODE = 40013;
    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDto> resourceNotFound(ResourceNotFoundException e, Locale locale) {
        String errorMessage = messageSource.getMessage((e.getMessage()), new Object[]{}, locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDto(errorMessage, NOT_FOUND_CUSTOM_CODE)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> validationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDto(Objects.requireNonNull(e.getFieldError()).toString(), BAD_REQUEST_CUSTOM_CODE)
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> validationException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDto(e.getMessage(), BAD_REQUEST_CUSTOM_CODE)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> validationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder("incorrect value = ");

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            message.append(violation.getInvalidValue().toString()).append(". ");
            message.append(violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDto(message.toString(), VALIDATION_CUSTOM_CODE)
        );
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ExceptionDto> psqlException(PSQLException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                new ExceptionDto(e.getMessage(), NOT_ACCEPTABLE_CUSTOM_CODE)
        );
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionDto> badRequest(Throwable e) {
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body (
                new ExceptionDto( e.getMessage(), BAD_REQUEST_CUSTOM_CODE )
        );
    }
}
