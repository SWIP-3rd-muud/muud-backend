package com.muud.auth.service;

import com.muud.auth.domain.dto.*;
import com.muud.auth.exception.AuthException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.muud.auth.jwt.JwtToken;
import com.muud.auth.jwt.JwtTokenUtils;
import com.muud.user.entity.LoginType;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.muud.auth.exception.AuthErrorCode.*;

@Transactional
@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    /**
     * 주어진 사용자 ID에 해당하는 사용자 정보를 반환합니다.
     *
     * @param userId 사용자 ID
     * @return User 사용자 객체
     * @throws AuthException 사용자가 존재하지 않으면 예외를 발생시킵니다.
     */
    @Transactional(readOnly = true)
    public User getLoginUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(INVALID_TOKEN::defaultException);
    }

    /**
     * 이메일을 통해 새로운 사용자를 등록합니다.
     *
     * @param request 사용자 등록 요청 정보
     * @return Long 등록된 사용자 ID
     * @throws AuthException 이메일이 중복되면 예외 발생
     */
    public Long signupWithEmail(SignupRequest request) {
        validateEmail(request.email());
        User user = buildUser(request);
        return userRepository.save(user).getId();
    }

    /**
     * 어드민 권한을 가진 사용자를 등록합니다.
     *
     * @param request 어드민 사용자 등록 요청 정보
     * @return Long 등록된 어드민 사용자 ID
     * @throws AuthException 이메일이 중복되면 예외 발생
     */
    public Long signupAdmin(SignupRequest request){
        validateEmail(request.email());
        User user = buildUser(request);
        user.grantAdminAuth();
        return userRepository.save(user).getId();
    }

    /**
     * 이메일과 비밀번호를 통해 사용자 인증을 수행하고, JWT 토큰을 발급합니다.
     *
     * @param signinRequest 로그인 요청 정보
     * @return SigninResponse 로그인 결과와 토큰 정보를 담은 응답 객체
     * @throws AuthException 이메일이 없거나 비밀번호가 틀리면 예외 발생
     */
    public SigninResponse signinWithEmail(SigninRequest signinRequest){
        User user = userRepository.findByEmail(signinRequest.email())
                .orElseThrow(EMAIL_NOT_FOUND::defaultException);

        if(passwordEncoder.matches(signinRequest.password(), user.getPassword())) {
            return generateSigninResponse(user, false);
        } else {
            throw PASSWORD_INCORRECT.defaultException();
        }
    }

    /**
     * 카카오 로그인 정보를 사용하여 사용자 인증을 수행하고, JWT 토큰을 발급합니다.
     *
     * @param kakaoInfoResponse 카카오 사용자 정보
     * @return SigninResponse 로그인 결과와 토큰 정보를 담은 응답 객체
     */
    public SigninResponse signinWithKakao(KakaoInfoResponse kakaoInfoResponse){
        User user = userRepository.findByEmailAndSocialId(kakaoInfoResponse.email(), kakaoInfoResponse.socialId())
                .orElse(kakaoInfoResponse.toEntity());
        boolean isNewUser = false;
        if(user.getId() == null){
            user = userRepository.save(user);
            isNewUser = true;
        }
        return generateSigninResponse(user, isNewUser);
    }

    /**
     * 주어진 요청을 기반으로 새로운 사용자 객체를 생성합니다.
     *
     * @param request 사용자 등록 요청 정보
     * @return User 새로 생성된 사용자 객체
     */
    private User buildUser(SignupRequest request) {
        return User.builder()
                .email(request.email())
                .nickname(request.nickname())
                .password(passwordEncoder.encode(request.password()))
                .loginType(LoginType.EMAIL)
                .build();
    }

    /**
     * 주어진 이메일이 이미 존재하는지 확인하고, 존재하면 예외를 발생시킵니다.
     *
     * @param email 사용자 이메일
     * @throws AuthException 이메일이 이미 존재하는 경우 예외 발생
     */
    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw EMAIL_ALREADY_EXISTS.defaultException();
        }
    }

    private SigninResponse generateSigninResponse(User user, boolean isNewUser) {
        JwtToken token = jwtTokenUtils.generateToken(user);
        user.updateRefreshToken(token.refreshToken());
        return SigninResponse.of(token.accessToken(), token.refreshToken(), user.toDto(), isNewUser);
    }

    /**
     *  refreshToken을 사용하여 새로운 JWT 토큰을 발급합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return TokenResponse 새로운 JWT 토큰을 담은 응답 객체
     * @throws AuthException 리프레시 토큰이 유효하지 않거나 만료된 경우 예외 발생
     */
    public TokenResponse reIssueToken(String refreshToken) {
        jwtTokenUtils.validateToken(refreshToken);
        Long userId = Long.valueOf(jwtTokenUtils.getUserIdFromToken(refreshToken));
        User user = getLoginUser(userId);
        if (user.validRefreshToken(refreshToken)) {
            String newToken = jwtTokenUtils.reIssueToken(user);
            return TokenResponse.of(newToken);
        } else {
            throw TOKEN_EXPIRED.defaultException();
        }
    }

}