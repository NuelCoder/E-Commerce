package com.example.pcuhub.pcuhub.service.USER;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.dtos.UserKafkaMessage;
import com.example.pcuhub.pcuhub.entities.PendingUser;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.enums.ActionType;
import com.example.pcuhub.pcuhub.enums.Role;
import com.example.pcuhub.pcuhub.exceptions.BadInputException;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.kafka.KafkaProducerService;
import com.example.pcuhub.pcuhub.repository.PendingUserRepository;
import com.example.pcuhub.pcuhub.repository.UserRepository;
import com.example.pcuhub.pcuhub.service.OTP.OTPService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OTPService oTPService;
    private final PendingUserRepository pendingUserRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;


    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${app.admin-secret}")
    private String adminSecret;

    public void verifyOtpAndFinalizeRegistration(String email, String otp) throws BadRequestException{
        if (!oTPService.validateRegistrationOtp(email, otp)) {
            throw new BadInputException("OTP is either invalid or expired");
        }
        PendingUser pendingUser = pendingUserRepository.findByEmail(email)
        .orElseThrow(() -> new BadRequestException("Email not found"));

        User user = new User();
        user.setName(pendingUser.getName());
        user.setEmail(pendingUser.getEmail());
        user.setPassword(pendingUser.getPassword());

        if (adminSecret.equals(pendingUser.getAdminPassword())) {
            user.setRole(Role.MARKETER);
        }
        else{
            user.setRole(Role.BUYER);
        }   
        
        userRepository.save(user);
        sendKafkaMessage(user, ActionType.SAVED);
        pendingUserRepository.delete(pendingUser);
        oTPService.clearOtp(email);


        
    }
    private void sendKafkaMessage(User user, ActionType actionType) {
    UserKafkaMessage message = new UserKafkaMessage();
    message.setId(user.getUserId());
    message.setName(user.getName());
    message.setEmail(user.getEmail());
    message.setActionType(actionType);
    message.setSource("USER");
    message.setCreatedAt(user.getCreatedAt());

    kafkaProducerService.sendUserKafkaMessage(message);
}
    
    public void processKafkaUserAction(UserKafkaMessage message) {
        switch (message.getActionType()) {
            case SAVED -> logger.info("👤 Kafka Event - User Saved: {}", message.toString());
            case UPDATED -> logger.info("✏️ Kafka Event - User Updated: {}", message.toString());
            case DELETED -> logger.info("❌ Kafka Event - User Deleted: {}", message.toString());
            default -> logger.warn("⚠️ Unknown Kafka Action Type: {}", message.getActionType());
        }


     }

    public Optional<User> getUserByEmail(String email){
        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException("User with this email doesn't exist");
        }
        return userRepository.findByEmail(email);
     }

    public void deleteUserByEmail(String email){
        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException(email + " does not exist");
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        userRepository.delete(user);
        sendKafkaMessage(user, ActionType.DELETED);
       }
     }

    public User updateUserPartially(Long id, Map<String,Object>updates){
        
        User user = userRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("User can not be found"));

        updates.forEach((key,value) ->{
            if (key.equalsIgnoreCase("password")) {
                throw new BadRequestException("You can not update password through the patch method");
            }
            try {
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(user,value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Invalid Field: " + key);
            }
        });

        sendKafkaMessage(user, ActionType.UPDATED);
        return userRepository.save(user);
    }

    public void deleteUser(String email){
        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email does not exist");
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            sendKafkaMessage(user, ActionType.DELETED);
        }
    }
}
