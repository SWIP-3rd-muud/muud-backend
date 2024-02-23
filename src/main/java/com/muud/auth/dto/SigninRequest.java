package com.muud.auth.dto;

import lombok.Getter;

@Getter
public class SigninRequest {
    private String email;
    private String password;
}