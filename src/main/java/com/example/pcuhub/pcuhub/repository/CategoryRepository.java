package com.example.pcuhub.pcuhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pcuhub.pcuhub.entities.Category;



@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    Optional<Category>findByCategoryId(Long categoryId);

    Optional<Category>findByCategoryName(String categoryName);

    void deleteByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
    
}
