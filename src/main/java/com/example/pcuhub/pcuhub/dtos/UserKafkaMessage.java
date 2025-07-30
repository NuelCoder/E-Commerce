package com.example.pcuhub.pcuhub.dtos;

import java.time.LocalDateTime;

import com.example.pcuhub.pcuhub.enums.ActionType;
import com.example.pcuhub.pcuhub.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKafkaMessage {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String adminSecret;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ActionType actionType;
    private String source; 

   @Override
    public String toString() {
        return "UserKafkaMessage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", adminSecret=" + adminSecret +'\'' +
                ", actionType='" + actionType + '\'' +
                ", createdAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt + '\'' +
                ", source=" + source + '\'' +
                '}';
    }
}
