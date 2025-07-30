package com.example.pcuhub.pcuhub.controllers;

import java.util.HashMap;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pcuhub.pcuhub.service.CART.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pcuhub.pcuhub.entities.CartItem;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RequestMapping("/api/v1/carts")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<Map<String,Object>> addToCart(@RequestParam Long productId, @RequestBody int quantity) {
        
        CartItem cartItem = cartService.addToCart(productId, quantity);
        Map<String,Object> response = new HashMap<>();

        response.put("Message", "Item has been added ");
        response.put("Cart", cartItem);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/item")
    public ResponseEntity<Map<String,Object>> getCartItemsWithTotalValue() {
      return ResponseEntity.ok(cartService.getUserCartItemsWithTotal());
    }
    
    @DeleteMapping("/remove-item")
    public ResponseEntity<Map<String,Object>> removeItemFromCart(Long productId){
        cartService.removeItemByProductId(productId);

        Map<String,Object> response = new HashMap<>();
        response.put("message", "Item removed successfully");
        response.put("productId", productId);

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/increase/{id}")
    public ResponseEntity<CartItem> increaseQuantity(@RequestParam Long productId, @RequestParam int quantityToAdd) {
       
       CartItem updatedCartItem = cartService.increaseProductQuantityInCart(productId, quantityToAdd);
       
        return ResponseEntity.ok(updatedCartItem);
    }

    @PutMapping("/decrease/{id}")
    public ResponseEntity<CartItem> decreaseQuantity(@RequestParam Long productId, @RequestParam int quantityToReduce) {
       
        CartItem updatedCartItem = cartService.decreaseProductQuantityInCart(productId, quantityToReduce);
        
        return ResponseEntity.ok(updatedCartItem);
    }
}
