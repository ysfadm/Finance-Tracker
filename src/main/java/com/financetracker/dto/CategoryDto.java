package com.financetracker.dto;

import lombok.*;

/**
 * Category DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String color;
}
