package com.financetracker.controller;

import com.financetracker.dto.ApiResponseDto;
import com.financetracker.dto.LoginRequestDto;
import com.financetracker.dto.LoginResponseDto;
import com.financetracker.dto.RegisterRequestDto;
import com.financetracker.entity.User;
import com.financetracker.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Authentication controller for handling user login and registration
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<User>> register(@Valid @RequestBody RegisterRequestDto request) {
        User user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(user, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Login successful"));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponseDto<String>> validateToken() {
        return ResponseEntity.ok(ApiResponseDto.success("Valid token", "Token is valid"));
    }
}
