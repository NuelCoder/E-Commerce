package com.example.pcuhub.pcuhub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="cart_items")
public class CartItem {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Min(value= 1, message="Quantity must be at least 1")
    @Column(nullable= false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name="user_id", nullable= false)
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id", nullable= false)
    private Product product;

    public double getTotalPrice(){
        return product.getPrice() * quantity;
    }
}

