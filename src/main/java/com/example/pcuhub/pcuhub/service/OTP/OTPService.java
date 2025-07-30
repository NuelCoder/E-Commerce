package com.example.pcuhub.pcuhub.service.OTP;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.repository.PendingUserRepository;
import com.example.pcuhub.pcuhub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final Map<String,Object> otpstore = new HashMap<>();
    private final Map<String, LocalDateTime> expirystore = new HashMap<>();
    private final Map<String, LocalDateTime> lastsentstore = new HashMap<>();
    private final PendingUserRepository pendingUserRepository;
    private final UserRepository userRepository;
   

    private final int OTP_EXPITY_MINUTES = 10;
    private final int OTP_COOLDOWN_SECONDS = 60;

    public String generateRegistrationOtp(String email){
        if (pendingUserRepository.findByEmail(email).isEmpty()) {
            throw new BadRequestException("No pending user found for email: " + email);
        }
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpstore.put(email, otp);
        expirystore.put(email, LocalDateTime.now().plusMinutes(OTP_EXPITY_MINUTES));
        return otp;
    }
    

    public boolean validateRegistrationOtp(String email, String otp){
        if (!otpstore.containsKey(email)) {
            return false;
        }
        if (expirystore.get(email).isBefore(LocalDateTime.now())) {
            return false;
        }
        return otpstore.get(email).equals(otp);
    }
    
    
    public String refreshRegistrationOtp(String email){
        if (pendingUserRepository.findByEmail(email).isEmpty()) {
            throw new BadRequestException("User with " + email + " does not exist");
        }
        
        if (lastsentstore.containsKey(email)) {
            LocalDateTime lastSentTime = lastsentstore.get(email);
            if (lastSentTime.plusSeconds(OTP_COOLDOWN_SECONDS).isAfter(LocalDateTime.now())) {
                throw new BadRequestException("OTP was recently sent. Please wait before refreshing.");
            }
        }
        String newOtp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpstore.put(email, newOtp);
        expirystore.put(email, LocalDateTime.now().plusMinutes(OTP_EXPITY_MINUTES));
        lastsentstore.put(email, LocalDateTime.now());
        
        return newOtp;
    }
    
    public String generateForgotPasswordOtp(String email){
        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException("User does not exist");
        }
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpstore.put(email, otp);
        expirystore.put(email, LocalDateTime.now().plusMinutes(OTP_EXPITY_MINUTES));
        return otp;
    }
    
    public boolean validatePasswordOtp(String email, String otp){
        if (!otpstore.containsKey(email)) {
            return false;
        }
        if(!expirystore.containsKey(email)){
            return false;
        }
        if (expirystore.get(email).isBefore(LocalDateTime.now())) {
            return false;
        }
        return otpstore.get(email).equals(otp);
    }
    public void clearOtp(String email){
        otpstore.remove(email);
        expirystore.remove(email);
    }
}
