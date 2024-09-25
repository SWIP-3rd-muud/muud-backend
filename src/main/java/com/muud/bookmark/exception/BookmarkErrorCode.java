package com.muud.bookmark.exception;

import com.muud.global.exception.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {

    BOOKMARK_ALREADY_EXISTS("이미 추가된 북마크입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("북마크 추가 중 해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DIARY_NOT_FOUND("북마크 추가 중 해당 일기를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BOOKMARK_NOT_FOUND("북마크를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DEFAULT("북마크 취급 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

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
    public BookmarkException defaultException() {
        return new BookmarkException(this);
    }

    @Override
    public BookmarkException defaultException(Throwable cause) {
        return new BookmarkException(this, cause);
    }
}
