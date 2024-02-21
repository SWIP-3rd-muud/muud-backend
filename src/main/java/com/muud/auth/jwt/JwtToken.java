package com.muud.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class JwtToken {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
