package com.muud.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class UserInfo {
    private Long id;
    private String nickname;
}
