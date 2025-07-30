package com.example.pcuhub.pcuhub.service.PRODUCT;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.repository.CategoryRepository;
import com.example.pcuhub.pcuhub.repository.ProductRepository;

import org.springframework.security.access.AccessDeniedException;

import com.example.pcuhub.pcuhub.dtos.ProductKafkaMessage;
import com.example.pcuhub.pcuhub.entities.Category;
import com.example.pcuhub.pcuhub.entities.Product;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.enums.ActionType;
import com.example.pcuhub.pcuhub.enums.Role;
import com.example.pcuhub.pcuhub.exceptions.BadInputException;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.kafka.KafkaProducerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaProducerService kafkaProducerService;

    private final Logger logger =LoggerFactory.getLogger(ProductService.class);

    public Product createProduct(Product product, String categoryName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not Authenticated");
        }
        User user = (User) authentication.getPrincipal();
        if (user.getRole() != Role.MARKETER) {
            throw new BadRequestException("Only Marketers can create Products");
        }
        Category category= categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new BadRequestException(categoryName + " does not exist"));
        
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        sendKafkaMessage(product, ActionType.CREATED);
        return savedProduct;


    }
    private void sendKafkaMessage(Product product, ActionType actionType) {
    ProductKafkaMessage message = new ProductKafkaMessage();
    message.setId(product.getId());
    message.setProductName(product.getProductName());
    message.setDescription(product.getDescription());
    message.setPrice(product.getPrice());
    message.setActionType(actionType);
    message.setCreatedAt(product.getCreatedAt());

    kafkaProducerService.sendProductKafkaMessage(message);
    }
    public Optional <Product> getProductByProductId(Long productId){
       if (!productRepository.existsByProductId(productId)) {
            throw new BadRequestException( "Product does not exist");
       }
       return productRepository.findByProductId(productId);
    }

    public List<Product> getProductByPriceRange(Double minPrice, Double maxPrice){
        if (minPrice < 0 || maxPrice < 0) {
            throw new BadInputException("Prices can not be negative");
        }
        if (minPrice > maxPrice) {
            throw new BadRequestException("Minimum Price can not be greater than Maximum Price");
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> getProductsByMinPrice(Double minPrice){
        if (minPrice < 0) {
            throw new BadRequestException("Price cannot be negative");
        }
        return productRepository.findByPriceGreaterThanEqual(minPrice);
    }

    public List<Product> getProductsByMaxPrice(Double maxPrice){
        if (maxPrice < 0) {
            throw new BadInputException("Price can not be negative");
        }
        return productRepository.findByPriceLessThanEqual(maxPrice);
    }
    
    public Product patchProduct(Long id, Map<String,Object>updates){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();
        if (user.getRole() != Role.MARKETER) {
            throw new BadRequestException("Only Marketers can update a product");
        }
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("Product does not exist"));

        if (!product.getCreatedBy().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only delete products you created");
        }
        updates.forEach((key,value) ->{
            try {
                Field field = Product.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(product, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Invalid Field: " + key);
            }
        });


        Product savedProduct = productRepository.save(product);
        sendKafkaMessage(product, ActionType.UPDATED);
        return savedProduct;
        
    }

    public void deleteProductByProductId(Long productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = (User)authentication.getPrincipal();
        if (user.getRole()!= Role.MARKETER) {
            throw new BadRequestException("Only marketers can delete ");
        }
        Optional <Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isEmpty()) {
            throw new BadRequestException("Product does not exist");
        }
        Product product = optionalProduct.get();

        if (!product.getCreatedBy().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only delete products you created");
        }
        productRepository.delete(product);
        sendKafkaMessage(product, ActionType.DELETED);
    }
          public void processKafkaProductAction(ProductKafkaMessage message) {
        switch (message.getActionType()) {
            case CREATED -> logger.info("👤 Kafka Event - Product Saved: {}", message.toString());
            case UPDATED -> logger.info("✏️ Kafka Event - Product Updated: {}", message.toString());
            case DELETED -> logger.info("❌ Kafka Event - Product Deleted: {}", message.toString());
            default -> logger.warn("⚠️ Unknown Kafka Action Type: {}", message.getActionType());
        }


     }
}
