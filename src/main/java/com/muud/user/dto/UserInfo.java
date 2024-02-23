package com.muud.user.dto;

import lombok.Builder;
import lombok.Getter;
@Getter @Builder
public class UserInfo {
    private Long id;
    private String nickname;
}
