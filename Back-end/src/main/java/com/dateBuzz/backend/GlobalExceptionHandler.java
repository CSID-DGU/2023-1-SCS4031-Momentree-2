package com.dateBuzz.backend;

import com.dateBuzz.backend.controller.recommend.RecommendController;
import com.dateBuzz.backend.controller.responseDto.ErrorResponse;
import com.dateBuzz.backend.exception.DateBuzzException;
import com.dateBuzz.backend.util.TelegramNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
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

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleMyException(Exception ex) {
        log.error("에러 메시지: {}", ex.getMessage(), ex);
        String errorMessage = ex.getMessage();
        telegramNotifier.sendErrorMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
