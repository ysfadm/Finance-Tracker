package com.financetracker.repository;

import com.financetracker.entity.Category;
import com.financetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Category repository for database operations
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
    List<Category> findByUserAndType(User user, Category.CategoryType type);
}
