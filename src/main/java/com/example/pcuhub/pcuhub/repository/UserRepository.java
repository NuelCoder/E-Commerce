package com.example.pcuhub.pcuhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pcuhub.pcuhub.entities.User;



public interface  UserRepository extends JpaRepository<User, Long>{
    Optional<User>findByUserId(Long userId);
    Optional<User>findByEmail(String email);
    Optional<User>findByName(String name);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    
}
