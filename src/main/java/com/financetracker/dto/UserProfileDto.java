package com.financetracker.dto;

import lombok.*;

/**
 * User profile DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String currency;
}
