package com.taxgapdetection.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {

            Class<?> targetType = ife.getTargetType();

            if (targetType.equals(LocalDate.class)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Invalid request",
                        "message", "Invalid date format,Expected YYYY-MM-DD"
                ));
            }

            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid request",
                    "message", "Invalid value Type"
            ));
        }
        //fallback handler for invalid request body
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Malformed JSON",
                "message", "Request body is invalid"
        ));
    }
    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<String> handleDuplicateTransaction(
            DuplicateTransactionException ex){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

}
