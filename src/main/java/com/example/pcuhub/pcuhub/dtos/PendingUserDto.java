package com.example.pcuhub.pcuhub.dtos;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingUserDto {
    private String name;

    private String email;
    
    private String password;

    private String adminPassword;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
