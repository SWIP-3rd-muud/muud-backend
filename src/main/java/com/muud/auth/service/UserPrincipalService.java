package com.muud.auth.service;

import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;
    /**
     * @param username
     * @return
     * @throws ApiException
     */
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ApiException(ExceptionType.INVALID_AUTHENTICATE));
        return new UserPrincipal(user);
    }

}
