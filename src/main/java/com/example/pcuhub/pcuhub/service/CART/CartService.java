package com.example.pcuhub.pcuhub.service.CART;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.entities.CartItem;
import com.example.pcuhub.pcuhub.entities.Product;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.repository.CartItemRepository;
import com.example.pcuhub.pcuhub.repository.ProductRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItem addToCart(Long productId, int quantity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User Not Authenticated");
        }

        User user = (User)authentication.getPrincipal();

    
        Product product = productRepository.findByProductId(productId)
            .orElseThrow(() -> new BadRequestException("Product by marketer not found"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setUser(user);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }
    
    public Map<String,Object> getUserCartItemsWithTotal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    
        Map<String, Object> response = new HashMap<>();
        response.put("items", cartItems);
        response.put("total", total);

        return response;

    }

    public void removeItemByProductId(Long productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = (User)authentication.getPrincipal();
        Product product = productRepository.findByProductId(productId)
            .orElseThrow(() -> new BadRequestException( "Product does not exist"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (cartItem == null) {
            throw new BadRequestException("Product not in cart");
        }
        if (!cartItem.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only delete items in your cart");
        }
        cartItemRepository.delete(cartItem);
    }

    public CartItem increaseProductQuantityInCart(Long productId, int quantityToAdd){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = (User)authentication.getPrincipal();

        Product product = productRepository.findByProductId(productId)
            .orElseThrow(() -> new BadRequestException("Product does not exist"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        
        cartItem.setQuantity(cartItem.getQuantity() + quantityToAdd);
        return cartItemRepository.save(cartItem);
    }

    public CartItem decreaseProductQuantityInCart( Long productId, int quantityToReduce){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();

          Product product = productRepository.findByProductId(productId)
            .orElseThrow(() -> new BadRequestException("Product does not exist"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        
    if (cartItem.getQuantity() <= quantityToReduce) {
        cartItemRepository.delete(cartItem);
        return null;
    } else {
        cartItem.setQuantity(cartItem.getQuantity() - quantityToReduce);
        return cartItemRepository.save(cartItem);
    }
}

    public double calculateTotalCartValue(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        return cartItems.stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
    }
}
