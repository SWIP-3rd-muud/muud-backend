package com.muud.testpackage;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private String name;
    private int age;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.age = user.getAge();
    }
}
