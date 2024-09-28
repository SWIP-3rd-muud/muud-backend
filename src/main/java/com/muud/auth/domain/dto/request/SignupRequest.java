package com.muud.auth.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일 양식에 맞게 작성해주세요")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이내로 만들어야 합니다.")
        String password,

        @Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이내로 작성해야 합니다.")
        String nickname
) {
}
