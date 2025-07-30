package com.example.pcuhub.pcuhub.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ResetPasswordDTO {
    private String email;
    private String otp;
    private String password;
}
