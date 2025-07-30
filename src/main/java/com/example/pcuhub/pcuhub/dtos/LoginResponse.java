package com.example.pcuhub.pcuhub.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginResponse {
    public String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse() {} // default constructor for deserialization
}

