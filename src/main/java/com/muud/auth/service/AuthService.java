//package com.muud.auth.service;
//
//import com.muud.auth.dto.SigninRequest;
//import com.muud.auth.dto.SigninResponse;
//import com.muud.auth.dto.SignupRequest;
//import com.muud.auth.jwt.JwtToken;
//import com.muud.auth.jwt.JwtTokenUtils;
//import com.muud.user.entity.LoginType;
//import com.muud.user.entity.User;
//import com.muud.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Transactional(readOnly = true)
//@Service @RequiredArgsConstructor
//public class AuthService {
//    private final UserRepository userRepository;
//    private final JwtTokenUtils jwtTokenUtils;
//    private final PasswordEncoder passwordEncoder;
//    public Optional<User> getUserByEmail(String email){
//        return userRepository.findByEmail(email);
//    }
//
//    @Transactional
//    public User signupWithEmail(SignupRequest request) {
//        User user = User.builder()
//                .email(request.getEmail())
//                .nickname(request.getNickname())
//                .password(passwordEncoder.encrypt(request.getEmail(), request.getPassword()))
//                .loginType(LoginType.EMAIL)
//                .build();
//        return userRepository.save(user);
//    }
//
//    public SigninResponse signinWithEmail(SigninRequest signinRequest) throws Exception {
//        //확인 후 토큰 발급
//        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();
//        System.out.println(user.getNickname());
//        if(user.getPassword().equals(passwordEncoder.encrypt(signinRequest.getEmail(), signinRequest.getPassword()))){
//            JwtToken token = jwtTokenUtils.createToken(user.getEmail(), user.getNickname());
//            return SigninResponse.builder()
//                    .accessToken(token.getAccessToken())
//                    .refreshToken(token.getRefreshToken())
//                    .userInfo(user.toDto())
//                    .build();
//        }else{
//            throw new Exception("비밀번호가 틀렸습니다.");
//        }
//    }
//
//
//}
