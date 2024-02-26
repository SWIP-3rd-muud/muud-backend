package com.muud.auth.controller;

import com.muud.auth.dto.KakaoInfoResponse;
import com.muud.auth.dto.SigninRequest;
import com.muud.auth.dto.SigninResponse;
import com.muud.auth.dto.SignupRequest;
import com.muud.auth.service.AuthService;
import com.muud.auth.service.KakaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KakaoService kakaoService;
    @PostMapping("/auth/signup")
    public ResponseEntity signupWithEmail(@Valid @RequestBody SignupRequest request){
        authService.signupWithEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "회원가입이 완료되었습니다."));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<SigninResponse> signinWithEmail(@Valid @RequestBody SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signinWithEmail(signinRequest);
        return ResponseEntity.ok().body(signinResponse);
    }

    @PostMapping("/auth/kakao/signin")
    public ResponseEntity signinWithKakao(@RequestBody Map<String, String> mapCode){
        KakaoInfoResponse kakaoInfoResponse = kakaoService.getKakaoInfo(mapCode.get("code"));
        SigninResponse signinResponse = authService.signinWithKakao(kakaoInfoResponse);
        if(signinResponse.isNewUser){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(signinResponse);
        }
        return ResponseEntity.ok(signinResponse);
    }
}
