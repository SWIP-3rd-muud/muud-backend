package com.muud.user.service;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.global.util.SecurityUtils;
import com.muud.user.dto.UserInfo;
import com.muud.user.dto.UserInfoUpdateRequest;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.muud.auth.exception.AuthErrorCode.ACCESS_DENIED;
import static com.muud.auth.exception.AuthErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfo changeUserNickname(Long userId, UserInfoUpdateRequest infoUpdateRequest) {
        if(!SecurityUtils.checkCurrentUserId(userId)) {
            throw ACCESS_DENIED.defaultException();
        }

        String nickname = infoUpdateRequest.nickname();
        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND::defaultException);
        user.updateNickname(nickname);

        return user.toDto();
    }

}
