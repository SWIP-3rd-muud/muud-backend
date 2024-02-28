package com.muud.auth.jwt;

import com.muud.auth.service.AuthService;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.dto.UserInfo;
import com.muud.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthService authService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 토큰이 없으면 요청 거부
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ApiException(ExceptionType.ACCESS_DENIED_EXCEPTION);
        }else{
            token = token.substring(7, token.length());
        }
        User user = authService.getUserById(Long.valueOf(jwtTokenUtils.getUserIdFromToken(token)))
                .orElseThrow(() -> new ApiException(ExceptionType.INVALID_TOKEN));
        request.setAttribute("user", user);
        return true;
    }
}