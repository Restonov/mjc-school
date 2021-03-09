package com.epam.esm.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for Exception handler
 *
 */
@Data
public class ExceptionDto {

    private String errorMessage;
    private int errorCode;
    private LocalDateTime errorTime;

    public ExceptionDto(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorTime = LocalDateTime.now();
    }
}
