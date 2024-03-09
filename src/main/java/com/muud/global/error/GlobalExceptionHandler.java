package com.muud.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ResponseError> handleGlobalException(ApiException e) {
        return ResponseEntity.status(e.getExceptionType().getStatus())
                .body(new ResponseError(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(ExceptionType.INVALID_INPUT_VALUE.getStatus())
                .body(new ResponseError(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}