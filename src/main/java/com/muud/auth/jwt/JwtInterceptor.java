package com.muud.auth.jwt;

import com.muud.auth.service.AuthService;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.dto.UserInfo;
import com.muud.user.entity.Authority;
import com.muud.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

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
        Auth auth = ((HandlerMethod)handler).getMethodAnnotation(Auth.class);
        if(auth == null){
            return true;
        }else{
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith("Bearer ")) {
                throw new ApiException(ExceptionType.ACCESS_DENIED_EXCEPTION);
            }else{
                token = token.substring(7, token.length());
            }
            User user = authService.getUserById(Long.valueOf(jwtTokenUtils.getUserIdFromToken(token)))
                    .orElseThrow(() -> new ApiException(ExceptionType.INVALID_TOKEN));
            if(auth.role().compareTo(Auth.Role.ADMIN)==0 && !user.getRole().equals(Authority.ROLE_ADMIN)){
                throw new ApiException(ExceptionType.FORBIDDEN_USER);
            }
            request.setAttribute("user", user);
        }

        return true;
    }
}