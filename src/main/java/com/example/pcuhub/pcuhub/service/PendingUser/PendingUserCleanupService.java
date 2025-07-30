package com.example.pcuhub.pcuhub.service.PendingUser;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.repository.PendingUserRepository;

@Service
public class PendingUserCleanupService {

    @Autowired
    private final PendingUserRepository pendingUserRepository;

    private static final Logger logger = LoggerFactory.getLogger(PendingUserCleanupService.class);

    public PendingUserCleanupService(PendingUserRepository pendingUserRepository) {
        this.pendingUserRepository = pendingUserRepository;
    }
    
    @Scheduled(fixedRate = 10 * 10000)
    public void cleanUpExpiredPendingUsers(){
        LocalDateTime cutoff = LocalDateTime.now().minusHours(2);
        pendingUserRepository.deleteAllByCreatedAtBefore(cutoff);
        logger.info("[CLEANUP] Deleted {} expired pending users older than {}\", deleted, cutoff");
        
    }
}
