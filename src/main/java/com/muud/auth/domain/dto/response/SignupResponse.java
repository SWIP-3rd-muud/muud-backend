package com.muud.auth.domain.dto.response;

public record SignupResponse(String message) {

    private static final String DEFAULT_SUCCESS_MESSAGE = "회원가입이 완료되었습니다.";

    public static SignupResponse of() {
        return new SignupResponse(DEFAULT_SUCCESS_MESSAGE);
    }

    public static SignupResponse of(String message) {
        return new SignupResponse(message);
    }
}
