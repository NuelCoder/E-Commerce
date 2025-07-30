package com.example.pcuhub.pcuhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pcuhub.pcuhub.entities.Order;
import com.example.pcuhub.pcuhub.entities.User;





@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    
    Optional<Order> findByOrderId(Long orderId);

    List<Order> findAllByUser(User user);

    boolean existsByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);
}
