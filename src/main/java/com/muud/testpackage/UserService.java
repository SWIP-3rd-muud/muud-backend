package com.muud.testpackage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getUserList() {

        List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }
}
