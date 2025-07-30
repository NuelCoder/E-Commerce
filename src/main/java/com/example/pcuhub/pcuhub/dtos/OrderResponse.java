package com.example.pcuhub.pcuhub.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private LocalDate createdAt;
    private double totalAmount;
    private String status;
    private List<OrderItemResponse> items;
}
