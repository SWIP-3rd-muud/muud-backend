package com.muud.auth.domain.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muud.user.dto.UserInfo;

public record SigninResponse(String accessToken,
                             String refreshToken,
                             UserInfo userInfo,
                             @JsonIgnore boolean isNewUser) {

    public static SigninResponse of(String accessToken, String refreshToken, UserInfo userInfo, Boolean isNewUser) {
        return new SigninResponse(accessToken, refreshToken, userInfo, isNewUser);
    }
}