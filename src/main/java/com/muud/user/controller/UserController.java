package com.muud.user.controller;

import com.muud.auth.jwt.Auth;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.dto.UserInfo;
import com.muud.user.entity.User;
import com.muud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static com.muud.auth.jwt.Auth.Role.USER;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Auth()
    @PatchMapping("/users/{userId}/nickname")
    public ResponseEntity updateUserNickname(@RequestAttribute User user, @PathVariable("userId") Long userId, @RequestBody Map<String, String> mapNickname){
        if(!user.getId().equals(userId)){
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
        UserInfo userInfo = userService.changeUserNickname(userId, mapNickname.get("nickname"));
        return ResponseEntity.ok(userInfo);
    }
}
