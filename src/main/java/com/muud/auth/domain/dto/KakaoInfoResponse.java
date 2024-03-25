package com.muud.auth.domain.dto;

import com.muud.user.entity.LoginType;
import com.muud.user.entity.User;
public record KakaoInfoResponse(String socialId, String email, String nickname) {

    public static KakaoInfoResponse of(String socialId, String email, String nickname) {
        return new KakaoInfoResponse(socialId, email, nickname);
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .socialId(socialId)
                .loginType(LoginType.KAKAO)
                .build();
    }

}