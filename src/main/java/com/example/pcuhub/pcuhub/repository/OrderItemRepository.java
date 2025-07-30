package com.example.pcuhub.pcuhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pcuhub.pcuhub.entities.Order;
import com.example.pcuhub.pcuhub.entities.OrderItem;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    Optional<OrderItem>findByOrderItemId(Long orderItemId);

    List<OrderItem> findByOrder(Order order);
}
