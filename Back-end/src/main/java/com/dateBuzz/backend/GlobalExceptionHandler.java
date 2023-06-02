package com.dateBuzz.backend;

import com.dateBuzz.backend.controller.responseDto.ErrorResponse;
import com.dateBuzz.backend.exception.DateBuzzException;
import com.dateBuzz.backend.util.TelegramNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final TelegramNotifier telegramNotifier;

    @ExceptionHandler(DateBuzzException.class)
    @ResponseBody
    public ErrorResponse<HttpStatus> handleMyException(DateBuzzException ex) {
        String errorMessage = ex.getErrorCode().getMessage();
        HttpStatus status = ex.getErrorCode().getStatus();
        return ErrorResponse.error(errorMessage, status);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<String> handleMyException(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        telegramNotifier.sendErrorMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러, 수정 요망");
    }

}
