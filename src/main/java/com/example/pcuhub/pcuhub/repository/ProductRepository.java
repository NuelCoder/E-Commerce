package com.example.pcuhub.pcuhub.repository;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pcuhub.pcuhub.entities.Product;
import com.example.pcuhub.pcuhub.entities.User;
import com.example.pcuhub.pcuhub.enums.Role;



public interface ProductRepository extends JpaRepository<Product, Long>{
    
    Optional<Product> findByProductId(Long productId);

    Optional<Product> findByProductName(String productName);

@Query("""
    SELECT p FROM Product p
    WHERE p.productName = :productName
      AND p.createdBy.name = :marketerName
      AND p.createdBy.role = :role
""")
Optional<Product> findByProductNameAndMarketer(
    @Param("productName") String productName,
    @Param("marketerName") User createdBy,
    @Param("role") Role role
);

    Optional<Product>findByProductNameAndCreatedBy_Name(String productName, User createdBy, int quantity);

    List<Product> findByCategory(Category category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByPriceGreaterThanEqual(Double price);

    List<Product> findByPriceLessThanEqual(Double price);

    boolean existsByProductName(String productName);

    boolean existsByProductId(Long productId);

    void deleteByProductName(String productName);
}
