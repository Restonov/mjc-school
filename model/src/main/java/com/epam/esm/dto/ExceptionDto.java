package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for Exception handler
 *
 */
@Data
@AllArgsConstructor
public class ExceptionDto {

    private String errorMessage;
    private int errorCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime errorTime;

    public ExceptionDto(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorTime = LocalDateTime.now();
    }
}
