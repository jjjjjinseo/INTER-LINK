package com.example.interlink.user.dto;

import com.example.interlink.user.domain.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String password;

    public UserDto(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword()
        );
    }
}
