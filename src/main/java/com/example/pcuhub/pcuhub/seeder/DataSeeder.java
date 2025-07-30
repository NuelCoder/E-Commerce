package com.example.pcuhub.pcuhub.seeder;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.pcuhub.pcuhub.config.CategorySeedConfig;
import com.example.pcuhub.pcuhub.entities.Category;
import com.example.pcuhub.pcuhub.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final CategorySeedConfig categorySeedConfig;

    @Override
    public void run(String... args)throws Exception{
        if (categoryRepository.count() == 0) {
            List<Category> categories = categorySeedConfig.getCategories()
            .stream()
            .map(name -> {
                Category category = new Category();
                category.setCategoryName(name);
                return category;
            })
            .toList();

            categoryRepository.saveAll(categories);
             System.out.println("✅ Categories seeded from config.");
        }
    }
}
