//package com.muud.auth.controller;
//import com.muud.auth.dto.SigninRequest;
//import com.muud.auth.dto.SigninResponse;
//import com.muud.auth.dto.SignupRequest;
//import com.muud.auth.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//    private final AuthService authService;
//
//    @PostMapping("/auth/email")
//    public ResponseEntity emailCheck(@RequestBody Map<String, String> request){
//        if(authService.getUserByEmail(request.get("email")).isPresent()){
//            return ResponseEntity.status(409).body("conflict");
//        }else{
//            return ResponseEntity.ok(null);
//        }
//    }
//    @PostMapping("/auth/signup")
//    public ResponseEntity signupWithEmail(@RequestBody SignupRequest request){
//        authService.signupWithEmail(request);
//        return ResponseEntity.ok(null);
//    }
//
//    @PostMapping("/auth/signin")
//    public ResponseEntity signinWithEmail(@RequestBody SigninRequest signinRequest){
//        try {
//            SigninResponse signinResponse = authService.signinWithEmail(signinRequest);
//            return ResponseEntity.ok().body(signinResponse);
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body("유효하지 않은 이메일 주소 또는 비밀번호입니다.");
//        }
//    }
//}
