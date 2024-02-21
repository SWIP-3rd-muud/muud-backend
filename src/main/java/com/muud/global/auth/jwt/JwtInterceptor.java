package com.muud.global.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component @RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //API 호출 전 토큰 검증
        
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
