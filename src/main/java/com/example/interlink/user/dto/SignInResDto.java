package com.example.interlink.user.dto;

import lombok.Data;

@Data
public class SignInResDto {
    private String accessToken;

    public SignInResDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
