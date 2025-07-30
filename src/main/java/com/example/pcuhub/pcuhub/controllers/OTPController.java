package com.example.pcuhub.pcuhub.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pcuhub.pcuhub.service.EMAIL.EmailService;
import com.example.pcuhub.pcuhub.service.OTP.OTPService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/v1/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;
    private final EmailService emailService;
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshOTP(@RequestParam String email) {
        String refreshOTP = otpService.refreshRegistrationOtp(email);    
        emailService.RefreshOTPMail(email, refreshOTP);
        return ResponseEntity.ok("A new OTP has been sent to " + email);
    }
    
}
