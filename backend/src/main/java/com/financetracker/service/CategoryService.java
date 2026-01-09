package com.financetracker.service;

import com.financetracker.dto.CategoryDto;
import com.financetracker.entity.Category;
import com.financetracker.entity.User;
import com.financetracker.repository.CategoryRepository;
import com.financetracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Category service for managing expense/income categories
 */
@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public CategoryDto createCategory(String userEmail, CategoryDto dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(Category.CategoryType.valueOf(dto.getType()))
                .color(dto.getColor() != null ? dto.getColor() : "#000000")
                .user(user)
                .build();

        Category saved = categoryRepository.save(category);
        return mapToDto(saved);
    }

    public List<CategoryDto> getUserCategories(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return categoryRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getCategoriesByType(String userEmail, String type) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return categoryRepository.findByUserAndType(user, Category.CategoryType.valueOf(type)).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setColor(dto.getColor());

        Category updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .type(category.getType().toString())
                .color(category.getColor())
                .build();
    }
}
