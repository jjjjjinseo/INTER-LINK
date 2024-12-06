package com.example.interlink.user.dto;

import com.example.interlink.user.domain.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;

    public UserDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}
