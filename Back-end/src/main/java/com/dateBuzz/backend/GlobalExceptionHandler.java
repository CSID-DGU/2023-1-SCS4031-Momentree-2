package com.dateBuzz.backend;

import com.dateBuzz.backend.controller.responseDto.ErrorResponse;
import com.dateBuzz.backend.exception.DateBuzzException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(DateBuzzException.class)
    @ResponseBody
    public ErrorResponse<HttpStatus> handleMyException(DateBuzzException ex) {
        String errorMessage = ex.getErrorCode().getMessage();
        HttpStatus status = ex.getErrorCode().getStatus();
        return ErrorResponse.error(errorMessage, status);
    }
}
