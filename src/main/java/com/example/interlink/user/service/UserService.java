package com.example.interlink.user.service;

import com.example.interlink.response.StateResponse;
import com.example.interlink.user.domain.User;
import com.example.interlink.user.dto.SignUpReqDto;
import com.example.interlink.user.dto.UserDto;
import com.example.interlink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto read(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserDto.fromEntity(user);
    }
    public UserDto readByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserDto.fromEntity(user);
    }

    public boolean checkEmail(String email){
        Optional<User> opUser = userRepository.findByEmail(email);
        return opUser.isEmpty();
    }

}
