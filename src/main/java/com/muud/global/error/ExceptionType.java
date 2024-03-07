package com.muud.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
public enum ExceptionType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_AUTHENTICATE(HttpStatus.UNAUTHORIZED, "유효하지 않은 이메일 주소 또는 비밀번호입니다."),
    NOT_REGISTERED(HttpStatus.FORBIDDEN, "회원가입이 필요합니다"),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "처리중 시스템 에러가 발생하였습니다.");
    private final HttpStatus status;
    private String message;

    ExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
