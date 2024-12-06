package com.example.interlink.user.service;

import com.example.interlink.jwt.util.JwtUtil;
import com.example.interlink.user.domain.User;
import com.example.interlink.user.dto.SignInReqDto;
import com.example.interlink.user.dto.SignInResDto;
import com.example.interlink.user.dto.SignUpReqDto;
import com.example.interlink.user.dto.UserDto;
import com.example.interlink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto signUp(SignUpReqDto dto) {
        String email = dto.getEmail();
        String name = dto.getName();
        String password = passwordEncoder.encode(dto.getPassword());
        if(!userService.checkEmail(email)){
            throw new IllegalArgumentException("중복된 이메일입니다");
        }
        User newUser = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
        userRepository.save(newUser);
        return UserDto.fromEntity(newUser);
    }

    public SignInResDto signIn(SignInReqDto signInReqDto){
        String email = signInReqDto.getEmail();
        UserDto userDto= userService.readByEmail(email);
        if (passwordEncoder.matches(signInReqDto.getPassword(), userDto.getPassword())) {
            return new SignInResDto(jwtUtil.generateAccessToken(userDto.getEmail(), userDto.getId()));
        }else {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}
