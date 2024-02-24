package com.muud.auth.error;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;

public class UserNotFoundException extends Exception {

    private String id;

    public UserNotFoundException(String id) {
        this.id = id;
    }

    public UserNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
