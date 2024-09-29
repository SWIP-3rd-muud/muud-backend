package com.muud.global.util;

import com.muud.auth.exception.AuthException;
import com.muud.auth.service.UserPrincipal;
import com.muud.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static com.muud.auth.exception.AuthErrorCode.INVALID_TOKEN;

public class SecurityUtils {

    /**
     * 현재 인증된 사용자 정보 반환
     *
     * @return User 현재 인증된 사용자 정보
     * @throws AuthException 사용자 정보가 없을 경우
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        }
        throw INVALID_TOKEN.defaultException();
    }

    /**
     * 현재 사용자와 주어진 사용자 ID 비교 및 검증
     *
     * @param userId 검증할 사용자 ID
     * @return boolean 현재 로그인한 userId면 true, 아니면 false
     */
    public static boolean checkCurrentUserId(Long userId) {
        User user = getCurrentUser();
        return user.getId().equals(userId);
    }
}
