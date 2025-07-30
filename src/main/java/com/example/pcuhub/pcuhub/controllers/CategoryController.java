package com.example.pcuhub.pcuhub.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pcuhub.pcuhub.entities.Category;
import com.example.pcuhub.pcuhub.service.CATEGORY.CategoryService;

import lombok.RequiredArgsConstructor;


@RequestMapping("api/v1/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {
        private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<Map<String,Object>> getCategoryByName(@RequestParam String categoryName) {
        Optional<Category> optionalCategory = categoryService.getCategoryByName(categoryName);
        if (optionalCategory.isEmpty()) {
            Map<String,Object>response = new HashMap<>();
            response.put("Message", "Category Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Map<String,Object> response = new HashMap<>();
        response.put("Message", "category retrieved");
        response.put("Category", optionalCategory.get());

        return ResponseEntity.ok(response);
    }

    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
    
}
