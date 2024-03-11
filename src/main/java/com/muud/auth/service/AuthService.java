package com.muud.auth.service;

import com.muud.auth.domain.dto.KakaoInfoResponse;
import com.muud.auth.domain.dto.SigninRequest;
import com.muud.auth.domain.dto.SigninResponse;
import com.muud.auth.domain.dto.SignupRequest;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.muud.auth.jwt.JwtToken;
import com.muud.auth.jwt.JwtTokenUtils;
import com.muud.user.entity.LoginType;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User getLoginUser(Long userId){
        return getUserById(userId)
                .orElseThrow(()->new ApiException(ExceptionType.INVALID_TOKEN));
    }
    public Optional<User> getUserById(Long userId){
        return userRepository.findById(userId);
    }
    @Transactional
    public Long signupWithEmail(SignupRequest request) {
        User user = buildUser(request);
        return userRepository.save(user).getId();
    }

    @Transactional
    public Long signupAdmin(SignupRequest request){
        User user = buildUser(request);
        user.grantAdminAuth();
        return userRepository.save(user).getId();
    }
    @Transactional
    public SigninResponse signinWithEmail(SigninRequest signinRequest){
        //확인 후 토큰 발급
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new ApiException(ExceptionType.INVALID_AUTHENTICATE));
        if(user.checkPassword(passwordEncoder.encrypt(signinRequest.getEmail(), signinRequest.getPassword()))){
            JwtToken token = jwtTokenUtils.generateToken(user); //로그인시 토큰 발급
            user.updateRefreshToken(token.refreshToken());
            return SigninResponse.of(token.accessToken(), token.refreshToken(), user.toDto(), false);
        }else{
            throw new ApiException(ExceptionType.INVALID_AUTHENTICATE);
        }
    }

    @Transactional
    public SigninResponse signinWithKakao(KakaoInfoResponse kakaoInfoResponse){
        User user = userRepository.findByEmailAndSocialId(kakaoInfoResponse.email(), kakaoInfoResponse.socialId())
                .orElse(kakaoInfoResponse.toEntity());
        boolean isNew = false;
        if(user.getId() == null){
            user = userRepository.save(user);
            isNew = true;
        }
        JwtToken token = jwtTokenUtils.generateToken(user);
        user.updateRefreshToken(token.refreshToken());
        return SigninResponse.of(token.accessToken(), token.refreshToken(), user.toDto(), isNew);
    }
    public User buildUser(SignupRequest request){
        if(getUserByEmail(request.getEmail()).isPresent())
            throw new ApiException(ExceptionType.ALREADY_EXIST_EMAIL);
        return User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encrypt(request.getEmail(), request.getPassword()))
                .loginType(LoginType.EMAIL)
                .build();
    }
    public String reIssueToken(String refreshToken){
        jwtTokenUtils.validToken(refreshToken);
        Long userId = Long.valueOf(jwtTokenUtils.getUserIdFromToken(refreshToken));
        User user = getLoginUser(userId);
        if(user.validRefreshToken(refreshToken)){
           return jwtTokenUtils.createToken(user, "access");
        }else{
            throw new ApiException(ExceptionType.TOKEN_EXPIRED);
        }
    }
    public HttpHeaders setTokenCookie(String token) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .path("/")
                .maxAge(60*60*24*7) // 쿠키 유효기간 7일로 설정
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add("Set-cookie", cookie.toString());

        return headers;
    }
}