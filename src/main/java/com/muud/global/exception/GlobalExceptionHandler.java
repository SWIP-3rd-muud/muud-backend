package com.muud.global.exception;


import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.global.error.ResponseError;
import com.muud.global.exception.response.ApiResponseError;
import com.muud.global.exception.support.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseError> handleCustomException(CustomException e) {
        ApiResponseError response = ApiResponseError.from(e);
        HttpStatus httpStatus = e
                .getErrorCode()
                .defaultHttpStatus();
        log.error(e.getMessage());
        return new ResponseEntity<>(response, httpStatus);
    }

    //todo 추후 삭제
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ResponseError> handleGlobalException(ApiException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getExceptionType().getStatus())
                .body(new ResponseError(e.getMessage()));
    }

    //todo 추후 변경
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(ExceptionType.INVALID_INPUT_VALUE.getStatus())
                .body(new ResponseError(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError> handleAllExceptions(Exception e) {
        ApiResponseError response = ApiResponseError.builder().build();
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
