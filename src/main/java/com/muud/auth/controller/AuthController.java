package com.muud.auth.controller;

import com.muud.auth.dto.SigninRequest;
import com.muud.auth.dto.SigninResponse;
import com.muud.auth.dto.SignupRequest;
import com.muud.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("auth/signup")
    public ResponseEntity<Map<String, String>> signupWithEmail(@RequestBody SignupRequest request){
        authService.signupWithEmail(request);
        return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다"));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity signinWithEmail(@RequestBody SigninRequest signinRequest) throws Exception {
        SigninResponse signinResponse = authService.signinWithEmail(signinRequest);
        return ResponseEntity.ok().body(signinResponse);
    }

}
