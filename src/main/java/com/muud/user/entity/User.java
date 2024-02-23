package com.muud.user.entity;
import com.muud.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 10)
    private String nickname;
    @Enumerated(EnumType.STRING)
    private LoginType loginType;
    private String socialId; //카카오 로그인시, 이메일 로그인 유저는 null

    @Builder
    public User(String email, String password, String nickname, LoginType loginType, String socialId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.socialId = socialId;
    }
}
