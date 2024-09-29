package com.muud.auth.exception;

import com.muud.global.exception.support.CustomException;
import com.muud.global.exception.support.ErrorCode;

public class AuthException extends CustomException {

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
