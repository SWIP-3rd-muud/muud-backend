package com.muud.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SigninRequest(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, max = 15)
        String password) {

}
