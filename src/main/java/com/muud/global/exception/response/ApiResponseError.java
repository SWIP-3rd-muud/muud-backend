package com.muud.global.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muud.global.exception.support.CustomException;
import com.muud.global.exception.support.ErrorCode;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 *
 * @param code 에러 코드
 * @param status HTTP 상태 코드
 * @param name 오류명
 * @param message 오류 메세지
 * @param error HTTP 상태 코드 이름
 * @param cause
 * @param timestamp 발생 시간
 */
@Builder
public record ApiResponseError(
        String code,
        Integer status,
        String name,
        String message,
        String error,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ApiSimpleError> cause,
        Instant timestamp
) {
    public static ApiResponseError from(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String errorName = exception.getClass().getName()
                .substring(exception.getClass().getName().lastIndexOf(".") + 1);
        String error = errorCode.defaultHttpStatus().name();

        return ApiResponseError.builder()
                .code(errorCode.name())
                .status(errorCode.defaultHttpStatus().value())
                .name(errorName)
                .message(exception.getMessage())
                .error(error)
                .cause(ApiSimpleError.listOfCauseSimpleError(exception.getCause()))
                .build();
    }

    public ApiResponseError {
        if (code == null) {
            code = "API_ERROR";
        }

        if (status == null) {
            status = 500;
        }

        if (name == null) {
            name = "ApiError";
        }

        if (message == null || message.isBlank()) {
            message = "API 오류 - 로그를 확인하세요.";
        }

        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}
