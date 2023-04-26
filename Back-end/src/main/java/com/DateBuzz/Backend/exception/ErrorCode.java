package com.DateBuzz.Backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_NAME(HttpStatus.CONFLICT, "이미 존재하는 유저 정보입니다.");
    private HttpStatus status;
    private String message;
}
