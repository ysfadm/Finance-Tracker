package com.financetracker.controller;

import com.financetracker.dto.ApiResponseDto;
import com.financetracker.dto.UserProfileDto;
import com.financetracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * User controller for managing user profile
 */
@RestController
@RequestMapping("/users")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> getProfile(Authentication authentication) {
        UserProfileDto profile = userService.getUserProfile(authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.success(profile, "Profile retrieved successfully"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> updateProfile(
            @RequestBody UserProfileDto request,
            Authentication authentication) {
        UserProfileDto profile = userService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponseDto.success(profile, "Profile updated successfully"));
    }
}
