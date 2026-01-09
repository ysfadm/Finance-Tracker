package com.financetracker.dto;

import lombok.*;

/**
 * Login response DTO with JWT token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String currency;
}
