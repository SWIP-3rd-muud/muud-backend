package com.muud.auth.dto;
import com.muud.user.dto.UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder @Getter
public class SigninResponse {
    private String accessToken;
    private String refreshToken;
    private UserInfo userInfo;

    @Builder
    public SigninResponse(String accessToken, String refreshToken,UserInfo userInfo) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
    }
}
