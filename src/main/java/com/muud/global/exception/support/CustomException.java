package com.muud.global.exception.support;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private static ErrorCode getDefaultErrorCode() {
        return DefaultErrorCodeHolder.DEFAULT_ERROR_CODE;
    }

    protected final ErrorCode errorCode;

    public CustomException() {
        super(getDefaultErrorCode().defaultMessage());
        this.errorCode = getDefaultErrorCode();
    }

    public CustomException(String message) {
        super(message);
        this.errorCode = getDefaultErrorCode();
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = getDefaultErrorCode();
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.defaultMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    private static class DefaultErrorCodeHolder {
        private static final ErrorCode DEFAULT_ERROR_CODE = new ErrorCode() {
            @Override
            public String name() {
                return "SERVER_ERROR";
            }

            @Override
            public HttpStatus defaultHttpStatus() {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

            @Override
            public String defaultMessage() {
                return "서버 오류";
            }

            @Override
            public CustomException defaultException() {
                return new CustomException(this);
            }

            @Override
            public CustomException defaultException(Throwable cause) {
                return new CustomException(this, cause);
            }
        };
    }
}
