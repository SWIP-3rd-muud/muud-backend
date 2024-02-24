package com.muud.auth.service;

import com.muud.auth.dto.KakaoInfoResponse;
import com.muud.auth.dto.SigninRequest;
import com.muud.auth.dto.SigninResponse;
import com.muud.auth.dto.SignupRequest;
import com.muud.auth.error.UserNotFoundException;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.dto.UserInfo;
import org.springframework.stereotype.Service;

import com.muud.auth.jwt.JwtToken;
import com.muud.auth.jwt.JwtTokenUtils;
import com.muud.user.entity.LoginType;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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

    @Transactional
    public User signupWithEmail(SignupRequest request) {
        if(getUserByEmail(request.getEmail()).isPresent()) //이메일 중복 체크
            throw new ApiException(ExceptionType.INVALID_INPUT_VALUE);
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encrypt(request.getEmail(), request.getPassword()))
                .loginType(LoginType.EMAIL)
                .build();
        return userRepository.save(user);
    }

    public SigninResponse signinWithEmail(SigninRequest signinRequest){
        //확인 후 토큰 발급
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new ApiException(ExceptionType.INVALID_AUTHENTICATE));

        if(user.checkPassword(passwordEncoder.encrypt(signinRequest.getEmail(), signinRequest.getPassword()))){
            JwtToken token = jwtTokenUtils.createToken(user.getEmail(), user.getNickname()); //로그인시 토큰 발급
            return SigninResponse.builder()
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .userInfo(UserInfo.builder().id(user.getId()).nickname(user.getNickname()).build()) //**추후 수정 필요
                    .build();
        }else{
            throw new ApiException(ExceptionType.INVALID_AUTHENTICATE);
        }
    }

    public SigninResponse signinWithKakao(KakaoInfoResponse kakaoInfoResponse) throws UserNotFoundException {
        Optional<User> findUser = userRepository.findByEmailAndSocialId(kakaoInfoResponse.getEmail(), kakaoInfoResponse.getSocialId());
        if(findUser.isEmpty()){
            throw new UserNotFoundException("회원가입이 필요합니다", kakaoInfoResponse.getSocialId());
        }
        User user = findUser.get();
        JwtToken token = jwtTokenUtils.createToken(user.getEmail(), user.getNickname());
        return SigninResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userInfo(UserInfo.builder().id(user.getId()).nickname(user.getNickname()).build()) //**추후 수정 필요
                .build();
    }


}