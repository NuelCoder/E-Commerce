package com.example.pcuhub.pcuhub.service.EMAIL;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void RegistrationOTPMail(String toMail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);
        message.setSubject("Your Registration OTP");
        message.setText("Dear user,\n\nYour OTP code is: " + otp + "\n\nThis code is valid for 10 minutes.\n\n- PCUHub Team");
    
        try {
            javaMailSender.send(message);
            System.out.println("OTP email sent to: " + toMail);
        } catch (MailException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email");
        }
    }
    public void RefreshOTPMail(String toMail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);
        message.setSubject("Your New Registration OTP");
        message.setText("Dear user,\n\nYour OTP code is: " + otp + "\n\nThis code is valid for 10 minutes.\n\n- PCUHub Team");
    
        try {
            javaMailSender.send(message);
            System.out.println("OTP email sent to: " + toMail);
        } catch (MailException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email");
        }
    }
    public void ChangePasswordOTP(String toMail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);
        message.setSubject("Change Password OTP");
        message.setText("Dear user,\n\nYour OTP code is: " + otp + "\n\nThis code is valid for 10 minutes.\n\n- PCUHub Team");

            try {
            javaMailSender.send(message);
            System.out.println("OTP email sent to: " + toMail);
        } catch (MailException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    public void AccountVerified(String toMail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);
        message.setSubject("Account Verification");
        message.setText("Dear user,\n\nYour acoount has been verified. and saved\n\n- PCUHUB Team");

                try {
            javaMailSender.send(message);
            System.out.println("Verification email sent to: " + toMail);
        } catch (MailException e) {
            System.err.println("Failed to send Verification email: " + e.getMessage());
            throw new RuntimeException("Failed to send Verification email");
        }
    }
        
   public void EmailToMarketers(String toMail, String emailSubject, String emailText){
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toMail);
    message.setSubject(emailSubject);
    message.setText(emailText);

    try {
            javaMailSender.send(message);
            System.out.println("Order email sent to: " + toMail);
    } catch (MailException e) {
            System.err.println("Failed to send Order email: " + e.getMessage());
            throw new RuntimeException("Failed to send Order email");
        }
   }
}
