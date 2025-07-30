package com.example.pcuhub.pcuhub.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private String description;
    private int quantity;
    private double price;
    private Long categoryName;
}
