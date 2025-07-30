package com.example.pcuhub.pcuhub.service.PASSWORD;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.repository.UserRepository;
import com.example.pcuhub.pcuhub.service.OTP.OTPService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResetPassword {
    private final OTPService otpService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void resetPassword(String email, String newPassword, String otp){
        if (!otpService.validatePasswordOtp(email,otp)) {
            throw new BadRequestException("Invalid or Expired OTP");
        }
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BadRequestException("User does not exist"));

        user.setPassword(passwordEncoder.encode(newPassword));

        otpService.clearOtp(email);
    }
}
