package com.muud.auth.jwt;

import com.muud.auth.service.AuthService;
import com.muud.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthService authService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(HttpMethod.OPTIONS.matches(request.getMethod())) { //cors
            return true;
        }
        System.out.println("인터셉터 실행");
        //API 호출 전 토큰 검증
        String token = jwtTokenUtils.resolveToken(request);
        if(!StringUtils.hasText(token)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String email = jwtTokenUtils.getUserEmail(token);
        Optional<User> findUser = authService.getUserByEmail(email);
        if(findUser.isPresent()){
            User user = findUser.get();
            if(jwtTokenUtils.validateToken(token)){
                //String refreshToken = jwtTokenUtils.createToken(user.getEmail(), user.getNickname()).getRefreshToken();
                request.setAttribute("email", email);
                return true;
            }else if(jwtTokenUtils.validateToken(user.getRefreshToken())){
                return true;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}