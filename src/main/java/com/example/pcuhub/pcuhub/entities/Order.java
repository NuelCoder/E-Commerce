package com.example.pcuhub.pcuhub.entities;

import java.time.LocalDate;
import java.util.List;

import com.example.pcuhub.pcuhub.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long orderId;

   private double totalprice;

   private LocalDate createdAt;

   @Enumerated(EnumType.STRING)
   private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name="user_id", nullable= false)
    private User user;

   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

}
