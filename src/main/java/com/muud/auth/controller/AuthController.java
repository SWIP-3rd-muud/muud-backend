package com.muud.auth.controller;

import com.muud.auth.domain.dto.request.RefreshTokenRequest;
import com.muud.auth.domain.dto.request.SigninRequest;
import com.muud.auth.domain.dto.request.SignupRequest;
import com.muud.auth.domain.dto.response.KakaoInfoResponse;
import com.muud.auth.domain.dto.response.SigninResponse;
import com.muud.auth.domain.dto.response.SignupResponse;
import com.muud.auth.domain.dto.response.TokenResponse;
import com.muud.auth.service.AuthService;
import com.muud.auth.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "사용자 인증 및 토큰 관리 API")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    @Operation(summary = "이메일로 회원가입", description = "이메일을 사용하여 새로운 사용자 계정을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponse> signupWithEmail(@Valid @RequestBody SignupRequest request){
        authService.signupWithEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SignupResponse.of());
    }

    @Operation(summary = "이메일로 로그인", description = "이메일을 사용하여 로그인하고 JWT 토큰을 발급받습니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "409", description = "이미 가입된 회원")
    @PostMapping("/auth/signin")
    public ResponseEntity<SigninResponse> signinWithEmail(@Valid @RequestBody SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signinWithEmail(signinRequest);
        return ResponseEntity.ok()
                .body(signinResponse);
    }

    @Operation(description = "카카오 인증 서버로부터 받은 코드로 사용자 정보를 받고 회원가입 및 로그인 후 JWT 토큰을 발급받습니다.", summary = "카카오 로그인")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 코드 및 입력값")
    @PostMapping("/auth/kakao/signin")
    public ResponseEntity<SigninResponse> signinWithKakao(@RequestBody Map<String, String> mapCode){
        KakaoInfoResponse kakaoInfoResponse = kakaoService.getKakaoInfo(mapCode.get("code"));
        SigninResponse signinResponse = authService.signinWithKakao(kakaoInfoResponse);
        if(signinResponse.isNewUser()){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(signinResponse);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(signinResponse);
    }

    @Operation(summary = "어드민 권한 회원가입", description = "어드민용 코드를 입력받아 관리자 계정을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @ApiResponse(responseCode = "403", description = "코드 불일치 및 접근 권한 없음")
    @ApiResponse(responseCode = "409", description = "이미 가입된 회원")
    @PostMapping("/auth/signup/admin")
    public ResponseEntity<SignupResponse> signupAdmin(@RequestBody SignupRequest signupRequest, @RequestHeader(name = "Auth_Code") String authCode){
        Long userId = authService.signupAdmin(authCode, signupRequest);
        return ResponseEntity.created(URI.create("/users/"+userId))
                .body(SignupResponse.of());
    }

    @Operation(summary = "토큰 재발급", description = "refresh token을 사용하여 새로운 access token을 발급받습니다.")
    @ApiResponse(responseCode = "201", description = "토큰 재발급 성공")
    @ApiResponse(responseCode = "401", description = "리프레시 토큰이 유효하지 않거나 누락됨")
    @PostMapping("/auth/refresh")
    public ResponseEntity<TokenResponse> reIssueToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        TokenResponse tokenResponse = authService.reIssueToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tokenResponse);
    }

}