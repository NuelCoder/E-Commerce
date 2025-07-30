package com.example.pcuhub.pcuhub.dtos;

import com.example.pcuhub.pcuhub.entities.Category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    @NotNull(message = "Product name is required")
    private String productName;

    @NotNull(message = "Product description is required")
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Available quantity must be zero or more")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Negative value is not allowed")
    private Double price;

    @NotNull(message = "Category Id is required")
    private Long categoryId;

    private Category category;

}
