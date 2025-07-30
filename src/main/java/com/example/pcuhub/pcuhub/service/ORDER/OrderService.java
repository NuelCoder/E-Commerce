package com.example.pcuhub.pcuhub.service.ORDER;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.entities.CartItem;
import com.example.pcuhub.pcuhub.entities.Order;
import com.example.pcuhub.pcuhub.entities.OrderItem;
import com.example.pcuhub.pcuhub.entities.Product;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.enums.OrderStatus;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.repository.CartItemRepository;
import com.example.pcuhub.pcuhub.repository.OrderItemRepository;
import com.example.pcuhub.pcuhub.repository.OrderRepository;
import com.example.pcuhub.pcuhub.service.EMAIL.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final EmailService emailService;


    public Order placeOrder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not Authenticated");
        }
        User user = (User)authentication.getPrincipal();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is Empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            totalPrice += orderItem.getQuantity() * orderItem.getPrice();

        }
        order.setTotalprice(totalPrice);
        order.setOrderItems(orderItems);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        cartItemRepository.deleteAll(cartItems);

        notifyMarketera(orderItems);

        return order;
    }

    private void notifyMarketera(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            User marketer = product.getCreatedBy();

            String emailSubject = "New Order for Your Product: " + product.getProductName();
            String emailContent = "Dear " + marketer.getName() + ",\n\n" +
                    "A customer has placed an order for your product " + product.getProductName() +
                    ".\nQuantity: " + orderItem.getQuantity() + "\nTotal Price: " + orderItem.getPrice();
            
            try {
                emailService.EmailToMarketers(marketer.getEmail(), emailSubject, emailContent);
            } catch (Exception e) {
               throw new RuntimeException("Error occurred");
            }
        }
    }

    public List<Order> getUserOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User no authenticated");
        }
        User user = (User)authentication.getPrincipal();
        return orderRepository.findAllByUser(user);
    }
}
