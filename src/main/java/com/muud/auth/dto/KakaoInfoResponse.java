package com.muud.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoInfoResponse {
    private String socialId; //프로젝트, 회원 당 고유한 번호, external ID로 사용
    private String email; //비즈니스 앱으로 신청해야 받아올 수 있음
    private String nickname;

    @Builder
    public KakaoInfoResponse(String socialId, String email, String nickname) {
        this.socialId = socialId;
        this.email = email;
        this.nickname = nickname;
    }
}