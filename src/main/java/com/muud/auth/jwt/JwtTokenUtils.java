package com.muud.auth.jwt;

import com.muud.auth.exception.AuthException;
import com.muud.user.entity.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Date;

import static com.muud.auth.exception.AuthErrorCode.*;

@RequiredArgsConstructor
@Component
public class JwtTokenUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final String TOKEN_TYPE = "Bearer ";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Http 헤더에서 JWT 토큰을 추출
     *
     * @param request 요청된 http request
     * @return JWT 토큰
     * @throws AuthException
     */
    public String getTokenFromHeader(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(TOKEN_TYPE)) {
            throw INVALID_TOKEN.defaultException();
        }else{
            return token.substring(7, token.length());
        }
    }

    /**
     * JWT Token parsing 후 유효성 검증
     *
     * @param token 검증 토큰
     * @return 유효 여부, 유효하면 true
     * @throws AuthException 토큰이 유효하지 않거나, 만료된 경우
     */
    public boolean validateToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw INVALID_TOKEN.defaultException();
        } catch (ExpiredJwtException e) {
            throw TOKEN_EXPIRED.defaultException();
        }
    }

    /**
     * JWT 토큰에서 이메일을 추출
     *
     * @param token JWT 토큰
     * @return 토큰에서 추출한 이메일
     * @throws AuthException 토큰이 유효하지 않거나, 만료된 경우 또는 이메일 추출에 실패한 경우
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return (String) claims.get("email");
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw INVALID_TOKEN.defaultException();
        } catch (ExpiredJwtException e) {
            throw TOKEN_EXPIRED.defaultException();
        }
    }

    /**
     * JWT 토큰을 생성
     *
     * @param user 토큰 발급 대상
     * @param issueDate 토큰 발급 날짜
     * @param expireTerm 토큰 유효 기간
     * @return 생성된 JWT 토큰
     */
    public String createToken(User user, Date issueDate, Long expireTerm){
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("nickname", user.getNickname());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issueDate)
                .setExpiration(new Date(issueDate.getTime() + expireTerm))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Access 토큰 재발급
     *
     * @param user 토큰 발급 대상
     * @return 갱신된 JWT Access 토큰
     */
    public String reIssueToken(User user){
        return createToken(user, new Date(), ACCESS_TOKEN_EXPIRE_TIME);
    }

    public JwtToken generateToken(User user) {
        Date now = new Date();
        String accessToken = createToken(user, now, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = createToken(user, now, REFRESH_TOKEN_EXPIRE_TIME);
        return JwtToken.of(accessToken, refreshToken, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody().getSubject();
        }catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw INVALID_TOKEN.defaultException();
        } catch (ExpiredJwtException e) {
            throw TOKEN_EXPIRED.defaultException();
        }
    }

}