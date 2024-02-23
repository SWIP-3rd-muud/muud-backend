package com.muud.global.error;

import lombok.Getter;

@Getter
public class ResponseError {
    private String message;

    public ResponseError(String message) {
        this.message = message;
    }
}
