package com.example.interlink.user.controller;

import com.example.interlink.user.dto.SignUpReqDto;
import com.example.interlink.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/sign-up")
    private ResponseEntity<?> create(@RequestBody SignUpReqDto signUpDto){
        return ResponseEntity.ok(authService.signUp(signUpDto));
    }
}
