package com.financetracker.service;

import com.financetracker.dto.TransactionDto;
import com.financetracker.entity.Category;
import com.financetracker.entity.Transaction;
import com.financetracker.entity.User;
import com.financetracker.repository.CategoryRepository;
import com.financetracker.repository.TransactionRepository;
import com.financetracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction service for managing financial transactions
 */
@Service
@Slf4j
@SuppressWarnings("null")
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public TransactionDto createTransaction(String userEmail, TransactionDto dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        LocalDateTime transactionDateTime = dto.getTransactionDate() != null
                ? dto.getTransactionDate().atStartOfDay()
                : LocalDateTime.now();

        Transaction transaction = Transaction.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .type(Transaction.TransactionType.valueOf(dto.getType()))
                .category(category)
                .user(user)
                .transactionDate(transactionDateTime)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToDto(saved);
    }

    public Page<TransactionDto> getUserTransactions(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<Transaction> transactions = transactionRepository.findByUser(user, pageable);

        return transactions.map(this::mapToDto);
    }

    public List<TransactionDto> getTransactionsByDateRange(String userEmail, LocalDate start, LocalDate end) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();

        return transactionRepository.findByUserAndTransactionDateBetween(user, startDateTime, endDateTime).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto updateTransaction(Long id, TransactionDto dto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setTitle(dto.getTitle());
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionDate(dto.getTransactionDate() != null
                ? dto.getTransactionDate().atStartOfDay()
                : transaction.getTransactionDate());

        Transaction updated = transactionRepository.save(transaction);
        return mapToDto(updated);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    private TransactionDto mapToDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .title(transaction.getTitle())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .type(transaction.getType().toString())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .transactionDate(transaction.getTransactionDate().toLocalDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
