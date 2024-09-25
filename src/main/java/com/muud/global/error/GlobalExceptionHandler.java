//package com.muud.global.error;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ApiException.class)
//    protected ResponseEntity<ResponseError> handleGlobalException(ApiException e) {
//        log.error(e.getMessage(), e);
//        return ResponseEntity.status(e.getExceptionType().getStatus())
//                .body(new ResponseError(e.getMessage()));
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error(e.getMessage(), e);
//        return ResponseEntity.status(ExceptionType.INVALID_INPUT_VALUE.getStatus())
//                .body(new ResponseError(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
//    }
//}