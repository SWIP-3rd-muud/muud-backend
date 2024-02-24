package com.muud.auth.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muud.user.dto.UserInfo;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder @Data
public class SigninResponse {
    private String accessToken;
    private String refreshToken;
    private UserInfo userInfo;
    @JsonIgnore
    public boolean isNewUser;
    @Builder
    public SigninResponse(String accessToken, String refreshToken,UserInfo userInfo, Boolean isNewUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
        this.isNewUser = isNewUser;
    }
}