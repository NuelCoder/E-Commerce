package com.example.pcuhub.pcuhub.service.PendingUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.dtos.PendingUserDto;
import com.example.pcuhub.pcuhub.dtos.UserKafkaMessage;
import com.example.pcuhub.pcuhub.entities.PendingUser;
import com.example.pcuhub.pcuhub.enums.ActionType;
import com.example.pcuhub.pcuhub.enums.Role;
import com.example.pcuhub.pcuhub.exceptions.BadInputException;
import com.example.pcuhub.pcuhub.exceptions.PendingUserException;
import com.example.pcuhub.pcuhub.kafka.KafkaProducerService;
import com.example.pcuhub.pcuhub.repository.PendingUserRepository;

@Service
public class PendingUserService {

    private final KafkaProducerService kafkaProducerService;
    private final PendingUserRepository pendingUserRepository;
    private final PasswordEncoder passwordEncoder;
    
    public PendingUserService(PendingUserRepository pendingUserRepository, PasswordEncoder passwordEncoder, KafkaProducerService kafkaProducerService) {
        this.pendingUserRepository = pendingUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaProducerService = kafkaProducerService;
    }

    private final Logger logger = LoggerFactory.getLogger(PendingUserService.class);

    public PendingUser savePendingUser(PendingUserDto pendingUserDto){
        if (pendingUserRepository.findByEmail(pendingUserDto.getEmail()).isPresent()) {
            throw new PendingUserException("This account is already awaiting verification");
        }
        if (!isValidEmail(pendingUserDto.getEmail())) {
            throw new BadInputException("Invalid Email Format");
        }
        if (!isValidPassword(pendingUserDto.getPassword())) {
            throw new BadInputException("Invalid Password Format");
        }
        if (pendingUserDto.getEmail().isBlank()) {
            throw new BadInputException("Please provide your email");
        }
        if (pendingUserDto.getPassword().isBlank()) {
            throw new BadInputException("Please provide your password");
        }
        if (pendingUserDto.getName().isBlank()) {
            throw new BadInputException("Please provide your name");
        }

        PendingUser pendingUser = new PendingUser();
        pendingUser.setName(pendingUserDto.getName());
        pendingUser.setEmail(pendingUserDto.getEmail());
        pendingUser.setPassword(passwordEncoder.encode(pendingUserDto.getPassword()));
        pendingUser.setCreatedAt(pendingUserDto.getCreatedAt());
        pendingUser.setAdminPassword(pendingUserDto.getAdminPassword());
        pendingUser.setRole(Role.PENDING);


        PendingUser savedPendingUser = pendingUserRepository.save(pendingUser);
        
        UserKafkaMessage message = new UserKafkaMessage();
        message.setId(savedPendingUser.getPendingUserId());
        message.setName(savedPendingUser.getName());
        message.setEmail(savedPendingUser.getEmail());
        message.setCreatedAt(savedPendingUser.getCreatedAt());
        message.setActionType(ActionType.CREATED);
        message.setRole(Role.PENDING);
        message.setSource("PENDING");
        
        kafkaProducerService.sendUserKafkaMessage(message);
        return savedPendingUser;

    } 

    public void handlePendingUser(UserKafkaMessage message){
        logger.info("Handling Kafka message for pending user: {}", message);
    }

    private boolean isValidPassword(String password){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.^&+=!]).{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    private boolean isValidEmail(String email){
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }
}

