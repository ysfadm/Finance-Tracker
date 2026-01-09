package com.financetracker.controller;

import com.financetracker.dto.ApiResponseDto;
import com.financetracker.dto.TransactionDto;
import com.financetracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        try {
            TransactionDto transaction = transactionService.createTransaction(authentication.getName(), request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success(transaction, "Transaction created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<TransactionDto>>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            Page<TransactionDto> transactions = transactionService.getUserTransactions(
                    authentication.getName(), page, size);
            return ResponseEntity.ok(ApiResponseDto.success(transactions, "Transactions retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponseDto<List<TransactionDto>>> getTransactionsByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Authentication authentication) {
        try {
            List<TransactionDto> transactions = transactionService.getTransactionsByDateRange(
                    authentication.getName(), start, end);
            return ResponseEntity.ok(ApiResponseDto.success(transactions, "Transactions retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TransactionDto>> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDto request) {
        try {
            TransactionDto transaction = transactionService.updateTransaction(id, request);
            return ResponseEntity.ok(ApiResponseDto.success(transaction, "Transaction updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok(ApiResponseDto.success(null, "Transaction deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
}
