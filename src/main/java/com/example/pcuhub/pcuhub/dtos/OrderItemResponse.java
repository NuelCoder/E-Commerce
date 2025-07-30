package com.example.pcuhub.pcuhub.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    

    private Long productId;
    private String productName;
    private int quantity;
    private double price;
}
