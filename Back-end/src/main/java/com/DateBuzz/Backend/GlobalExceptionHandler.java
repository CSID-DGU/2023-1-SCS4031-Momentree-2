package com.DateBuzz.Backend;

import com.DateBuzz.Backend.exception.DateBuzzException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(DateBuzzException.class)
    public ResponseEntity<String> handleMyException(DateBuzzException ex) {
        String errorMessage = ex.getErrorCode().getMessage();
        HttpStatus status = ex.getErrorCode().getStatus();
        return new ResponseEntity<>(errorMessage, status);
    }
}
