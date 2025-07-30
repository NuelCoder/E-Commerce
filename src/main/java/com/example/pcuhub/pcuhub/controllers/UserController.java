package com.example.pcuhub.pcuhub.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pcuhub.pcuhub.dtos.OtpVerificationRequest;
import com.example.pcuhub.pcuhub.dtos.PendingUserDto;
import com.example.pcuhub.pcuhub.entities.PendingUser;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.service.EMAIL.EmailService;
import com.example.pcuhub.pcuhub.service.OTP.OTPService;
import com.example.pcuhub.pcuhub.service.PendingUser.PendingUserService;
import com.example.pcuhub.pcuhub.service.USER.UserService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final  PendingUserService pendingUserService;
    private final UserService userService;
    private final OTPService oTPService;
    private final EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerUser(@RequestBody PendingUserDto pendingUserDto) {
      PendingUser puser =  pendingUserService.savePendingUser(pendingUserDto);

      String otp =  oTPService.generateRegistrationOtp(pendingUserDto.getEmail());

      emailService.RegistrationOTPMail(pendingUserDto.getEmail(), otp);

      Map<String,Object> response = new  HashMap<>();

      response.put("Message: ", "Form has been filled");

      response.put("User: ", puser);

      response.put("Alert: ", "OTP has been sent to " + pendingUserDto.getEmail());

      return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify")
    public ResponseEntity<Map<String,Object>> verifyAndRegisterUser(@RequestBody OtpVerificationRequest otpVerificationRequest) throws BadRequestException{

           
           userService.verifyOtpAndFinalizeRegistration(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtp());

           emailService.AccountVerified(otpVerificationRequest.getEmail());

           Map<String,Object> response = new HashMap<>();

           response.put("Message: ", "OTP has been verified and registration is successful");

           return ResponseEntity.ok(response);


}
@GetMapping("/retrieve")
public ResponseEntity<Map<String,Object>> getUser(@RequestParam String email) {
   Optional<User>optionalUser = userService.getUserByEmail(email);

   if (optionalUser.isEmpty()) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   Map<String,Object> response = new HashMap<>();

   response.put("Message", "User retrieved");
   response.put("User", optionalUser.get());

   return ResponseEntity.ok(response);
}

@PatchMapping("/{id}")
public ResponseEntity<User> updateUserPartially(@PathVariable long id, @RequestBody Map<String,Object>updates){
  User updateUser = userService.updateUserPartially(id, updates);
  return ResponseEntity.ok(updateUser);
}

@DeleteMapping("")
public ResponseEntity<Map<String,Object>> deleteUserByEmail(@RequestParam String email){
  Optional<User>optionalUser = userService.getUserByEmail(email);
    if (optionalUser.isEmpty()) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }
      userService.deleteUser(email);
      Map<String,Object> response = new HashMap<>();

      response.put("Message", email + " has been deleted");

      return ResponseEntity.ok(response);

}
}
