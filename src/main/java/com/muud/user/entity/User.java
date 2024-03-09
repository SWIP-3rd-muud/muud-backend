package com.muud.user.entity;
import com.muud.auth.dto.KakaoInfoResponse;
import com.muud.auth.dto.SignupRequest;
import com.muud.auth.jwt.Auth;
import com.muud.global.common.BaseEntity;
import com.muud.user.dto.UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(nullable = false, length = 10)
    private String nickname;
    @Enumerated(EnumType.STRING)
    private LoginType loginType;
    private String socialId; //카카오 로그인시, 이메일 로그인 유저는 null
    @Enumerated(EnumType.STRING)
    private Authority role;
    private String refreshToken;
    @Builder
    public User(String email, String password, String nickname, LoginType loginType, String socialId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.socialId = socialId;
        this.role = Authority.ROLE_USER;
    }
    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public UserInfo toDto(){
        return UserInfo.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }


    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void grantAdminAuth(){
        this.role = Authority.ROLE_ADMIN;
    }
    public boolean validRefreshToken(String refreshToken){
        if(refreshToken == null || !refreshToken.equals(this.refreshToken))
            return false;
        return true;
    }
}
