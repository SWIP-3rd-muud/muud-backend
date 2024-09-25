package com.muud.diary.exception;

import com.muud.global.exception.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode {

    DIARY_ACCESS_DENIED("해당 일기에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    DIARY_ALREADY_EXISTS("해당 날짜에 이미 작성된 일기가 있습니다.", HttpStatus.CONFLICT),
    DIARY_NOT_FOUND("해당 일기를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DEFAULT("일기 취급 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

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
    public DiaryException defaultException() {
        return new DiaryException(this);
    }

    @Override
    public DiaryException defaultException(Throwable cause) {
        return new DiaryException(this, cause);
    }
}
