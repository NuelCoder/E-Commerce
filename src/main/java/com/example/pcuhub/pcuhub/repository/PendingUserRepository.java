package com.example.pcuhub.pcuhub.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pcuhub.pcuhub.entities.PendingUser;



@Repository
public interface PendingUserRepository extends JpaRepository<PendingUser,Long>{
    void deleteAllByCreatedAtBefore(LocalDateTime cutoffTime);

    Optional<PendingUser>findByEmail(String email);

    boolean existsByEmail(String email);

    
}

