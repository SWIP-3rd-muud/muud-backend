package com.muud.auth.jwt;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.entity.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class JwtTokenUtilsTest {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private String secretKey;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        ReflectionTestUtils.setField(jwtTokenUtils, "secretKey", secretKey);

        user = User.builder()
                .email("test@example.com")
                .nickname("test")
                .build();
        setField(user, "id", 1L);

        token = jwtTokenUtils.createToken(user, new Date(), ACCESS_TOKEN_EXPIRE_TIME);
    }

    @Test
    void createTokenSuccess() {
        assertNotNull(token);
    }

    @Test
    void validateTokenSuccess() {
        assertTrue(jwtTokenUtils.validateToken(token));
    }

    @Test
    void validateTokenFail() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            jwtTokenUtils.validateToken(token+"1111");
        });
        assertEquals(ExceptionType.INVALID_TOKEN, exception.getExceptionType());
    }

    @Test
    void getEmailFromTokenSuccess() {
        String email = jwtTokenUtils.getEmailFromToken(token);
        assertNotNull(email);
        assertEquals(user.getEmail(), email);
    }


    @Test
    void getIdFromTokenSuccess() {
        String userId = jwtTokenUtils.getUserIdFromToken(token);
        assertNotNull(userId);
        assertEquals(user.getId(), Long.parseLong(userId));
    }

}
