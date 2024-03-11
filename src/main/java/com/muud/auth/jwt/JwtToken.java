package com.muud.auth.jwt;

public record JwtToken(String accessToken, String refreshToken, Long accessTokenExpiresIn) {
    public static JwtToken of(String accessToken, String refreshToken, Long accessTokenExpiresIn){
        return new JwtToken(accessToken, refreshToken, accessTokenExpiresIn);
    }
}