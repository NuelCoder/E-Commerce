package com.example.pcuhub.pcuhub.service.CATEGORY;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.entities.Category;
import com.example.pcuhub.pcuhub.exceptions.BadRequestException;
import com.example.pcuhub.pcuhub.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> getCategoryByName(String categoryName){
      if (!categoryRepository.existsByCategoryName(categoryName)) {
        throw new BadRequestException(categoryName + "does not exist");
      }
      return categoryRepository.findByCategoryName(categoryName);
    }

    public Category getCategoryWithProducts(String categoryName){
        Category category = categoryRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new BadRequestException(categoryName));

        category.getProducts().size();

        return category;
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
