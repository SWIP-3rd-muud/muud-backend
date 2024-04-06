package com.muud.auth.jwt;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
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

    public String createToken(User user, Date issueDate, Long expireTerm){
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("nickname", user.getNickname());
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(issueDate) // 토큰 발행 시간 정보
                .setExpiration(new Date(issueDate.getTime() + expireTerm)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                .compact();
    }

    public String reIssueToken(User user){
        return  createToken(user, new Date(), ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String getTokenFromHeader(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(TOKEN_TYPE)) {
            throw new ApiException(ExceptionType.ACCESS_DENIED_EXCEPTION);
        }else{
            return token.substring(7, token.length());
        }
    }

    public JwtToken generateToken(User user) {
        Date now = new Date();
        String accessToken = createToken(user, now, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = createToken(user, now, REFRESH_TOKEN_EXPIRE_TIME);
        return JwtToken.of(accessToken, refreshToken, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public boolean validToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token) //토큰 파싱
                    .getBody();
            return true;  //유효하다면 true 반환
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            e.printStackTrace();
            throw new ApiException(ExceptionType.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new ApiException(ExceptionType.TOKEN_EXPIRED);
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