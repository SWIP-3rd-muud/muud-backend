package com.muud.user.service;

import com.muud.auth.exception.AuthErrorCode;
import com.muud.auth.exception.AuthException;
import com.muud.auth.service.UserPrincipal;
import com.muud.global.util.SecurityUtils;
import com.muud.user.dto.UserInfo;
import com.muud.user.dto.UserInfoUpdateRequest;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    private User user;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .nickname("test")
                .password("encryptedPassword")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        userPrincipal = new UserPrincipal(user);

        // Mock SecurityContextHolder
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("UserInfoUpdateRequest를 받아 User Nickname을 성공적으로 변경")
    void changeUserNickname_success() {
        // Given
        Long userId = 1L;
        String newNickname = "newNickname";
        User user = mock(User.class);
        UserInfoUpdateRequest request = new UserInfoUpdateRequest(newNickname);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserInfo expectedUserInfo = mock(UserInfo.class);
        when(user.toDto()).thenReturn(expectedUserInfo);

        // When
        UserInfo result = userService.changeUserNickname(userId, request);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(user, times(1)).updateNickname(newNickname);
        assertEquals(expectedUserInfo, result);
    }

    @Test
    @DisplayName("User가 존재하지 않으면 USER_NOT_FOUND 발생")
    void changeUserNickname_userNotFound() {
        // Given
        Long userId = 1L;
        UserInfoUpdateRequest request = new UserInfoUpdateRequest("newNickname");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        AuthException exception = assertThrows(AuthException.class, () -> {
            userService.changeUserNickname(userId, request);
        });

        assertEquals(AuthErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("현재 유저와 변경하려는 UserID와 로그인 User가 다르면 INVALID_TOKEN 발생")
    void changeUserNickname_accessDenied() {
        // Given
        Long differentUserId = 2L;  // 다른 유저 ID
        UserInfoUpdateRequest request = new UserInfoUpdateRequest("newNickname");
        when(SecurityUtils.checkCurrentUserId(differentUserId)).thenReturn(false);

        // When & Then
        AuthException exception = assertThrows(AuthException.class, () -> {
            userService.changeUserNickname(differentUserId, request);
        });

        assertEquals(AuthErrorCode.INVALID_TOKEN, exception.getErrorCode());
    }

}
