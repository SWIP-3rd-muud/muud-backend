package com.muud.user.service;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.dto.UserInfo;
import com.muud.user.dto.UserInfoUpdateRequest;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

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

    @Test
    @DisplayName("UserInfoUpdateRequest를 받아 유저 닉네임을 변경")
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
    @DisplayName("유저가 존재하지 않으면 ApiException 발생")
    void changeUserNickname_userNotFound() {
        // Given
        Long userId = 1L;
        UserInfoUpdateRequest request = new UserInfoUpdateRequest("newNickname");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.changeUserNickname(userId, request);
        });

        assertEquals(ExceptionType.BAD_REQUEST, exception.getExceptionType());
        verify(userRepository, times(1)).findById(userId);
    }
}
