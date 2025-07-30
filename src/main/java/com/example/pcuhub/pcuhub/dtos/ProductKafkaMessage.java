package com.example.pcuhub.pcuhub.dtos;

import java.time.LocalDateTime;

import com.example.pcuhub.pcuhub.enums.ActionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductKafkaMessage {
    private Long id;
    private String productName;
    private String description;
    private Double price;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ActionType actionType;



   @Override
    public String toString() {
        return "ProductKafkaMessage{" +
                "id='" + id + '\'' +
                ", product_name='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity +'\'' +
                ", actionType='" + actionType + '\'' +
                ", createdAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt + '\'' +
                '}';
    }

}
