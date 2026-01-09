package com.financetracker.dto;

import lombok.*;

/**
 * Login request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    private String email;
    private String password;
}
