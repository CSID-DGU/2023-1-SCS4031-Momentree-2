package com.DateBuzz.Backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_NAME(HttpStatus.CONFLICT, "이미 존재하는 유저 정보입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다.");
    private HttpStatus status;
    private String message;
}
