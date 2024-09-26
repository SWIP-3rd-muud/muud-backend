package com.muud.bookmark.exception;

import com.muud.global.exception.support.CustomException;
import com.muud.global.exception.support.ErrorCode;

public class BookmarkException extends CustomException {

    public BookmarkException() {
        super();
    }

    public BookmarkException(String message) {
        super(message);
    }

    public BookmarkException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookmarkException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BookmarkException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
