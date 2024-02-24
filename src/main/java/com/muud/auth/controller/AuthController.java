package com.muud.auth.controller;

import com.muud.auth.dto.KakaoInfoResponse;
import com.muud.auth.dto.SigninRequest;
import com.muud.auth.dto.SigninResponse;
import com.muud.auth.dto.SignupRequest;
import com.muud.auth.error.UserNotFoundException;
import com.muud.auth.jwt.JwtToken;
import com.muud.auth.service.AuthService;
import com.muud.auth.service.KakaoService;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.global.error.ResponseError;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KakaoService kakaoService;
    @PostMapping("auth/signup")
    public ResponseEntity<Map<String, String>> signupWithEmail(@RequestBody SignupRequest request){
        authService.signupWithEmail(request);
        return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다"));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<SigninResponse> signinWithEmail(@RequestBody SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signinWithEmail(signinRequest);
        return ResponseEntity.ok().body(signinResponse);
    }

    @GetMapping("/auth/kakao/signin")
    public void signinWithKakao(HttpServletResponse response){
        try {
            response.sendRedirect(kakaoService.getKakaoAuth());
        } catch (IOException e) {
            throw new ApiException(ExceptionType.NOT_FOUND);
        }
    }

    //카카오 로그인 처리 -> 회원가입 안했으면 에러
    @GetMapping("/oauth2/callback/kakao") //인증(Authenticate)후 콜백 메소드
    public ResponseEntity kakaoAuthCallback(@RequestParam String code) throws UserNotFoundException {
        KakaoInfoResponse kakaoInfoResponse = kakaoService.getKakaoInfo(code);
        SigninResponse response = authService.signinWithKakao(kakaoInfoResponse);
            return ResponseEntity.ok()
                    .body(SigninResponse.builder()
                            .accessToken(response.getAccessToken())
                            .refreshToken(response.getRefreshToken())
                            .userInfo(response.getUserInfo()).build());
    }

    @PostMapping
    @ExceptionHandler(UserNotFoundException.class) //수정 예정
    protected ResponseEntity<Map<String, String>> handleNotRegisteredUser(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "회원가입이 필요합니다.", "kakao_idt", exception.getId()));
    }

}
