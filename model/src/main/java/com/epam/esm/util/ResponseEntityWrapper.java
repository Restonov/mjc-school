package com.epam.esm.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Wrap resources into ResponseEntity
 */
public class ResponseEntityWrapper {

    private ResponseEntityWrapper() {
    }

    public static ResponseEntity<Boolean> wrapBoolean(boolean operationResult){
        ResponseEntity<Boolean> responseEntity;
        if (operationResult) {
            responseEntity = ResponseEntity.status(HttpStatus.OK).build();
        } else {
            responseEntity = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return responseEntity;
    }
}
