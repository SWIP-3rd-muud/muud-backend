package com.muud.auth.jwt;

import com.muud.user.entity.Authority;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Auth {
    Role role() default Role.USER;
    enum Role {
        ADMIN, USER;
    }
}