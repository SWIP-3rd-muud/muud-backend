package com.muud.auth.jwt;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.entity.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;
    private static final String BEARER_TYPE = "bearer";
    // 토큰 유효시간 30분
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(User user, String type){
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("nickname", user.getNickname());
        Date now = new Date();
        Date tokenExpiresIn = type.equals("access") ?
                new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME) : new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(tokenExpiresIn) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                .compact();
    }
    public JwtToken generateToken(User user) {
        String accessToken = createToken(user, "access");
        String refreshToken = createToken(user, "refresh");

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token) //토큰 파싱
                    .getBody();
            return true;  //유효하다면 true 반환
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw new ApiException(ExceptionType.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApiException(ExceptionType.TOKEN_EXPIRED);
        }
    }
    public Claims parseJwtToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new ApiException(ExceptionType.INVALID_AUTHENTICATE);
        }
    }
    // 토큰에서 회원 Id 정보 추출
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody().getSubject();
        }catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw new ApiException(ExceptionType.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApiException(ExceptionType.TOKEN_EXPIRED);
        }
    }

}