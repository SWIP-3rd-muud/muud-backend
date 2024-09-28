package com.muud.auth.domain.dto;

public record TokenResponse(String accessToken) {

    public static TokenResponse of(String accessToken){
        return new TokenResponse(accessToken);
    }
}
