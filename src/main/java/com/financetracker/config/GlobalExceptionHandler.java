package com.financetracker.config;

import com.financetracker.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for consistent error responses
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error("Validation failed: " + errors));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<String>> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {
        log.warn("Bad credentials attempt");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDto.error("Invalid email or password"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<String>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto<String>> handleRuntime(
            RuntimeException ex, WebRequest request) {
        log.error("Runtime error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<String>> handleGeneral(
            Exception ex, WebRequest request) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error("An unexpected error occurred"));
    }
}
