package com.example.pcuhub.pcuhub.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    
    @NotNull(message = "UserId is required")
    private Long userId;
}
