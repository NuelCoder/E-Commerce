package com.example.pcuhub.pcuhub.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.pcuhub.pcuhub.entities.Product;
import com.example.pcuhub.pcuhub.service.PRODUCT.ProductService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create-products")
    public ResponseEntity<Map<String,Object>> createProduct(@RequestBody Product product, @RequestParam String categoryName) {
            Product createdProduct = productService.createProduct(product, categoryName);
            Map<String,Object> response = new HashMap<>();
            response.put("Message", "Product has been created");
            response.put("Product", createdProduct);

            return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String,Object>> updateProductPartially(@PathVariable Long id, @RequestBody Map<String,Object> updates){
        Product updatedProduct = productService.patchProduct(id, updates);
        Map<String,Object>response = new HashMap<>();
        response.put("Message", "Product has been updated");
        response.put("Updated Product: ", updatedProduct);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<Map<String,Object>> deleteProduct(@RequestParam Long productId){
        Optional<Product> optionalProduct = productService.getProductByProductId(productId);
        if (optionalProduct.isEmpty()) {
            Map<String,Object> response = new HashMap<>();
            response.put("Message", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        productService.deleteProductByProductId(productId);
        Map<String,Object> response = new HashMap<>();
        response.put("Message", "Product has been deleted");
        return ResponseEntity.ok(response);
    }
    
}
