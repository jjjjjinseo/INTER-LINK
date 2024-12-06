package com.example.interlink.user.dto;

import lombok.Data;

@Data
public class SignUpReqDto {
    private String email;
    private String password;
    private String name;
}
