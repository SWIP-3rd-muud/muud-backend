package com.muud.auth.service;

import com.muud.auth.domain.dto.*;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.muud.auth.jwt.JwtToken;
import com.muud.auth.jwt.JwtTokenUtils;
import com.muud.user.entity.LoginType;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getLoginUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new ApiException(ExceptionType.INVALID_TOKEN));
    }

    public Long signupWithEmail(SignupRequest request) {
        User user = buildUser(request);
        return userRepository.save(user).getId();
    }

    public Long signupAdmin(SignupRequest request){
        User user = buildUser(request);
        user.grantAdminAuth();
        return userRepository.save(user).getId();
    }

    public SigninResponse signinWithEmail(SigninRequest signinRequest){
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new ApiException(ExceptionType.INVALID_AUTHENTICATE));
        if(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            JwtToken token = jwtTokenUtils.generateToken(user);
            user.updateRefreshToken(token.refreshToken());
            return SigninResponse.of(token.accessToken(), token.refreshToken(), user.toDto(), false);
        } else {
            throw new ApiException(ExceptionType.INVALID_AUTHENTICATE);
        }
    }

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
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new ApiException(ExceptionType.ALREADY_EXIST_EMAIL);
        return User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .loginType(LoginType.EMAIL)
                .build();
    }

    public TokenResponse reIssueToken(String refreshToken){
        jwtTokenUtils.validateToken(refreshToken);
        Long userId = Long.valueOf(jwtTokenUtils.getUserIdFromToken(refreshToken));
        User user = getLoginUser(userId);
        if(user.validRefreshToken(refreshToken)){
           String token =  jwtTokenUtils.reIssueToken(user);
           return TokenResponse.of(token);
        }else{
            throw new ApiException(ExceptionType.TOKEN_EXPIRED);
        }
    }

}