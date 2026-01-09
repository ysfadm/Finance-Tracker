package com.financetracker.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transaction DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private String type;
    private Long categoryId;
    private String categoryName;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
}
