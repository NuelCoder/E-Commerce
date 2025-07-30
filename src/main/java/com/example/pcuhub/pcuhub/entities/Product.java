package com.example.pcuhub.pcuhub.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name= "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message= "Product name is required")
    @Column(nullable= false)
    private String productName;

    private String description;

    private double price;

    @Min(value=0, message="Quantity can not be negative")
    @Column(nullable= false)
    private int quantity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name= "category_id", nullable= false)
    private Category category;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable= false)
    private User createdBy;
    
    @OneToMany(mappedBy= "product", cascade= CascadeType.ALL)
    private List<Order> orders;

    private Double discountPercentage;
    private boolean isOnSale;

    public double getPrice() {
        return price;
    }
}
