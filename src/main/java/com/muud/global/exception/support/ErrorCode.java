package com.muud.global.exception.support;


import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String defaultMessage();
    HttpStatus defaultHttpStatus();
    RuntimeException defaultException();
    RuntimeException defaultException(Throwable cause);
}
