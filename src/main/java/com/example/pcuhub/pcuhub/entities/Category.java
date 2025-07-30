package com.example.pcuhub.pcuhub.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank(message= "Category name is required")
    @Column(nullable=false,unique=true)
    private String categoryName;

    @OneToMany(mappedBy= "category", cascade= CascadeType.ALL)
    private List<Product>products;

    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User createdBy;

}
