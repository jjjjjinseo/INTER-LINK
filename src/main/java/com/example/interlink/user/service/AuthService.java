package com.example.interlink.user.service;

import com.example.interlink.jwt.util.JwtUtil;
import com.example.interlink.user.domain.User;
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
    private final PasswordEncoder passwordEncoder;

    public UserDto signUp(SignUpReqDto dto) {
        String email = dto.getEmail();
        String name = dto.getName();
        String password = passwordEncoder.encode(dto.getPassword());

        User newUser = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();

        userRepository.save(newUser);

        return UserDto.fromEntity(newUser);
    }
}
