package com.financetracker.controller;

import com.financetracker.dto.ApiResponseDto;
import com.financetracker.dto.CategoryDto;
import com.financetracker.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category controller for managing expense/income categories
 */
@RestController
@RequestMapping("/categories")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<CategoryDto>> createCategory(
            @RequestBody CategoryDto request,
            Authentication authentication) {
        CategoryDto category = categoryService.createCategory(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(category, "Category created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CategoryDto>>> getCategories(Authentication authentication) {
        List<CategoryDto> categories = categoryService.getUserCategories(authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.success(categories, "Categories retrieved successfully"));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponseDto<List<CategoryDto>>> getCategoriesByType(
            @PathVariable String type,
            Authentication authentication) {
        List<CategoryDto> categories = categoryService.getCategoriesByType(authentication.getName(), type);
        return ResponseEntity.ok(ApiResponseDto.success(categories, "Categories retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto request) {
        CategoryDto category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponseDto.success(category, "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponseDto.success(null, "Category deleted successfully"));
    }
}
