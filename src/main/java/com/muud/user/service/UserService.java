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

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfo changeUserNickname(Long userId, UserInfoUpdateRequest infoUpdateRequest) {
        if(!SecurityUtils.checkCurrentUserId(userId)) {
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }

        String nickname = infoUpdateRequest.nickname();
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ApiException(ExceptionType.BAD_REQUEST));
        user.updateNickname(nickname);

        return user.toDto();
    }

}
