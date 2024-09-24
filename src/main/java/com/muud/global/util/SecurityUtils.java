package com.muud.global.util;

import com.muud.auth.service.UserPrincipal;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    /**
     * 현재 인증된 사용자 정보 반환
     *
     * @return User 현재 인증된 사용자 정보
     * @throws ApiException 사용자 정보가 없을 경우
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        }
        throw new ApiException(ExceptionType.INVALID_AUTHENTICATE);
    }

    /**
     * 현재 사용자와 주어진 사용자 ID 비교 및 검증
     *
     * @param userId 검증할 사용자 ID
     * @throws ApiException 사용자 정보가 일치하지 않을 경우
     */
    public static void validateUser(Long userId) {
        User user = getCurrentUser();
        if(!user.getId().equals(userId)) {
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
    }

}
