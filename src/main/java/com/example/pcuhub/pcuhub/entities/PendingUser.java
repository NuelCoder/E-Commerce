package com.example.pcuhub.pcuhub.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.pcuhub.pcuhub.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PendingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pendingUserId;
    @Column(nullable= false)
    private String name;

    @Column(nullable=false, unique=true)
    private String email;
    
    @Column(nullable=false)
    private String password;

  
    private String adminPassword;

    
    private String otp;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
