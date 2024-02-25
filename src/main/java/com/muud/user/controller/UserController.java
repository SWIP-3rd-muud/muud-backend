package com.muud.user.controller;

import com.muud.user.dto.UserInfo;
import com.muud.user.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PatchMapping("/users/{userId}/nickname")
    public ResponseEntity updateUserNickname(@PathVariable("userId") Long userId, @RequestBody Map<String, String> mapNickname){
        UserInfo userInfo = userService.changeUserNickname(userId, mapNickname.get("nickname"));
        return ResponseEntity.ok(userInfo);
    }
}
