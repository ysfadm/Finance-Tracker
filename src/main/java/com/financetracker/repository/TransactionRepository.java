package com.financetracker.repository;

import com.financetracker.entity.Transaction;
import com.financetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Transaction repository for database operations
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUser(User user, Pageable pageable);

    List<Transaction> findByUserAndTransactionDateBetween(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.user = ?1 AND t.category.id = ?2 ORDER BY t.transactionDate DESC")
    Page<Transaction> findByUserAndCategory(User user, Long categoryId, Pageable pageable);
}
