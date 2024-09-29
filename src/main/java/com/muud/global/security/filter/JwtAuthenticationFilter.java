package com.muud.auth.jwt;

import com.muud.auth.service.UserPrincipalService;
import com.muud.auth.service.UserPrincipal;
import com.muud.global.exception.support.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserPrincipalService principalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenUtils.getTokenFromHeader(request);
            if (token != null) {
                String email = jwtTokenUtils.getEmailFromToken(token);
                UserPrincipal userDetails = principalService.loadUserByUsername(email);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        }catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return path.startsWith("/auth/") ||
                path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

}
