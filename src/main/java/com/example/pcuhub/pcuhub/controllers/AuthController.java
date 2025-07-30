package com.example.pcuhub.pcuhub.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pcuhub.pcuhub.dtos.LoginRequest;
import com.example.pcuhub.pcuhub.dtos.LoginResponse;
import com.example.pcuhub.pcuhub.dtos.ResetPasswordDTO;
import com.example.pcuhub.pcuhub.security.JwtUtil;
import com.example.pcuhub.pcuhub.service.EMAIL.EmailService;
import com.example.pcuhub.pcuhub.service.OTP.OTPService;
import com.example.pcuhub.pcuhub.service.PASSWORD.ResetPassword;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final OTPService otpService;
    private final EmailService emailService;
    private final ResetPassword resetPassword;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

       return ResponseEntity.ok(new LoginResponse(token));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendResetOtp(@RequestParam String email) {
        String otp = otpService.generateForgotPasswordOtp(email);
        emailService.ChangePasswordOTP(email, otp);
        return ResponseEntity.ok("OTP has been sent to " + email);
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        resetPassword.resetPassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getOtp(), resetPasswordDTO.getPassword());
        return ResponseEntity.ok("Password Updated Successfully");

    }
    
}
