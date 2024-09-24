package com.muud.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 닉네임 변경 요청")
public record UserInfoUpdateRequest(
        @Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이내로 작성해야 합니다.")
        @Schema(description = "변경할 닉네임", example = "newNickname", required = true)
        String nickname
) {
}
