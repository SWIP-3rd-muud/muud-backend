package com.muud.diary.exception;

import com.muud.global.exception.support.CustomException;
import com.muud.global.exception.support.ErrorCode;

public class DiaryException extends CustomException {

    public DiaryException() {
        super();
    }

    public DiaryException(String message) {
        super(message);
    }

    public DiaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiaryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DiaryException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
