package com.muud.user.service;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.dto.UserInfo;
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

    public UserInfo changeUserNickname(Long userId, String nickname) {
        if(nickname.isEmpty() || nickname.length()>10){
            throw new ApiException(ExceptionType.BAD_REQUEST);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ApiException(ExceptionType.BAD_REQUEST));
        user.updateNickname(nickname);
        return user.toDto();
    }

}
