package com.muud.user.controller;

import com.muud.global.util.SecurityUtils;
import com.muud.user.dto.UserInfo;
import com.muud.user.dto.UserInfoUpdateRequest;
import com.muud.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userId}/nickname")
    @Operation(summary = "사용자 닉네임 변경", description = "해당 사용자의 닉네임을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공")
    @ApiResponse(responseCode = "403", description = "수정 접근 권한 없음")
    public ResponseEntity updateUserNickname(@PathVariable("userId") Long userId,
                                             @Valid @RequestBody UserInfoUpdateRequest userInfoUpdateRequest){
        UserInfo userInfo = userService.changeUserNickname(userId, userInfoUpdateRequest);

        return ResponseEntity.ok(userInfo);
    }
}
