package com.muud.playlist.exception;

import com.muud.global.exception.support.CustomException;
import com.muud.global.exception.support.ErrorCode;

public class PlayListException extends CustomException {

    public PlayListException() {
        super();
    }

    public PlayListException(String message) {
        super(message);
    }

    public PlayListException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayListException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PlayListException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
