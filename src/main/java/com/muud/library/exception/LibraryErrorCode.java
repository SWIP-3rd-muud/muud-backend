package com.muud.library.exception;

import com.muud.global.exception.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LibraryErrorCode implements ErrorCode {
    LIBRARY_NOT_FOUND("해당 보관함을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_USER("해당 보관함에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_ADDED_PLAYLIST("해당 플레이리스트가 보관함에 이미 존재합니다.", HttpStatus.CONFLICT),
    DEFAULT("보관함을 불러오던 중 에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;


    @Override
    public String defaultMessage() {
        return message;
    }

    @Override
    public HttpStatus defaultHttpStatus() {
        return status;
    }

    @Override
    public RuntimeException defaultException() {
        return new LibraryException(this);
    }

    @Override
    public RuntimeException defaultException(Throwable cause) {
        return new LibraryException(this, cause);
    }
}
