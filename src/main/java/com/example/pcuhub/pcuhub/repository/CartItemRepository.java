package com.example.pcuhub.pcuhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pcuhub.pcuhub.entities.CartItem;
import com.example.pcuhub.pcuhub.entities.Product;
import com.example.pcuhub.pcuhub.entities.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    Optional<CartItem> findByCartItemId(Long cartItemId);

    List<CartItem>findByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);

}
