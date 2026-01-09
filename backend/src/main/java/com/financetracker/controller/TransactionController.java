package com.financetracker.controller;

import com.financetracker.dto.ApiResponseDto;
import com.financetracker.dto.TransactionDto;
import com.financetracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Transaction controller for managing financial transactions
 */
@RestController
@RequestMapping("/transactions")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<TransactionDto>> createTransaction(
            @RequestBody TransactionDto request,
            Authentication authentication) {
        TransactionDto transaction = transactionService.createTransaction(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(transaction, "Transaction created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<TransactionDto>>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Page<TransactionDto> transactions = transactionService.getUserTransactions(
                authentication.getName(), page, size);
        return ResponseEntity.ok(ApiResponseDto.success(transactions, "Transactions retrieved successfully"));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponseDto<List<TransactionDto>>> getTransactionsByRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            Authentication authentication) {
        List<TransactionDto> transactions = transactionService.getTransactionsByDateRange(
                authentication.getName(), start, end);
        return ResponseEntity.ok(ApiResponseDto.success(transactions, "Transactions retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TransactionDto>> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDto request) {
        TransactionDto transaction = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(ApiResponseDto.success(transaction, "Transaction updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResponseDto.success(null, "Transaction deleted successfully"));
    }
}
