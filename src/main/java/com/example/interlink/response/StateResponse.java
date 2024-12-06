package com.example.interlink.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StateResponse {

    private int code;
    private String message;

    @Builder
    public StateResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}