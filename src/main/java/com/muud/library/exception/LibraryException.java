package com.muud.library.exception;

import com.muud.global.exception.support.CustomException;
import com.muud.global.exception.support.ErrorCode;

public class LibraryException extends CustomException {

    public LibraryException() {
        super();
    }

    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibraryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LibraryException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
