package com.muud.global.error;

public class ApiException extends RuntimeException{
    private ExceptionType exceptionType;

    public ApiException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
