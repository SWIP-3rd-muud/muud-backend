package com.muud.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ResponseError> handleGlobalException(ApiException e) {
        return ResponseEntity.status(e.getExceptionType().getStatus())
                .body(new ResponseError(e.getExceptionType().getMessage()));
    }
}
