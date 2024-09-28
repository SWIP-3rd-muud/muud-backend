package com.muud.auth.service;

import com.muud.auth.domain.dto.request.SigninRequest;
import com.muud.auth.domain.dto.request.SignupRequest;
import com.muud.auth.domain.dto.response.KakaoInfoResponse;
import com.muud.auth.domain.dto.response.SigninResponse;
import com.muud.auth.exception.AuthErrorCode;
import com.muud.auth.exception.AuthException;
import com.muud.auth.jwt.JwtToken;
import com.muud.auth.jwt.JwtTokenUtils;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private JwtToken token;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .nickname("test")
                .password("encryptedPassword")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        token = new JwtToken("accessToken", "refreshToken", 100000L);
    }

    private SignupRequest signupRequest() {
        return new SignupRequest("test@example.com", "password", "test");
    }

    private SigninRequest signinRequest() {
        return new SigninRequest("test@example.com", "password");
    }

    @Test
    @DisplayName("성공적으로 이메일 회원가입 처리")
    void signupWithEmail_success() {
        //given
        SignupRequest request = signupRequest();

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        // when
        Long userId = authService.signupWithEmail(request);

        //then
        assertEquals(user.getId(), userId);
        verify(userRepository, times(1))
                .findByEmail(request.email());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시도 시 EMAIL_ALREADY_EXISTS 반환")
    void signupWithEmail_fail_duplicateEmail() {
        // given
        SignupRequest request = signupRequest();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user)); // 이미 존재하는 이메일

        // when & then
        AuthException exception = assertThrows(AuthException.class, () -> authService.signupWithEmail(request));
        assertEquals(AuthErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("성공적으로 이메일 로그인 처리")
    void signinWithEmail_success() {
        //given
        SigninRequest request = signinRequest();
        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword()))
                .thenReturn(true);
        when(jwtTokenUtils.generateToken(user))
                .thenReturn(token);

        //then
        SigninResponse response = authService.signinWithEmail(request);
        assertEquals(response.accessToken(), token.accessToken());
        assertEquals(response.refreshToken(), token.refreshToken());
        assertEquals(response.userInfo().id(), user.getId());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않을 때 PASSWORD_INCORRECT 반환")
    void signinWithEmail_fail_incorrectPassword() {
        // given
        SigninRequest request = signinRequest();

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false); // 비밀번호 불일치

        // when & then
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.signinWithEmail(request);
        });
        assertEquals(AuthErrorCode.PASSWORD_INCORRECT, exception.getErrorCode());
        verify(passwordEncoder, times(1))
                .matches(anyString(), anyString());
    }

    @Test
    @DisplayName("성공적으로 카카오 회원가입 처리")
    void kakao_signup_success(){
        //given
        KakaoInfoResponse kakaoInfoResponse = new KakaoInfoResponse("123456789", "test@test.com", "test");
        when(userRepository.findByEmailAndSocialId(kakaoInfoResponse.email(), kakaoInfoResponse.socialId()))
                .thenReturn(Optional.empty()); // 기존에 없는 회원
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(jwtTokenUtils.generateToken(user))
                .thenReturn(token);

        //when
        SigninResponse response = authService.signinWithKakao(kakaoInfoResponse);

        //then
        assertEquals(response.accessToken(), token.accessToken());
        assertEquals(response.refreshToken(), token.refreshToken());
        assertEquals(response.userInfo().id(), user.getId());
        assertTrue(response.isNewUser());
    }

    @Test
    @DisplayName("성공적으로 카카오 로그인 처리")
    void kakao_signin_success(){
        // given
        KakaoInfoResponse kakaoInfoResponse = new KakaoInfoResponse("123456789", "test@test.com", "test");
        when(userRepository.findByEmailAndSocialId(kakaoInfoResponse.email(), kakaoInfoResponse.socialId()))
                .thenReturn(Optional.of(user));
        when(jwtTokenUtils.generateToken(user))
                .thenReturn(token);

        // when
        SigninResponse response = authService.signinWithKakao(kakaoInfoResponse);

        // then
        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        assertFalse(response.isNewUser());
        verify(userRepository, never()).save(any(User.class)); // Verify user is not saved as it's an existing user
    }

}