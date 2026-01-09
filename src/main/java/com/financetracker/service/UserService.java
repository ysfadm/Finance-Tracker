package com.financetracker.service;

import com.financetracker.dto.UserProfileDto;
import com.financetracker.entity.User;
import com.financetracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User service for managing user profile
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToDto(user);
    }

    public UserProfileDto updateProfile(String email, UserProfileDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setCurrency(dto.getCurrency());

        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    private UserProfileDto mapToDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .currency(user.getCurrency())
                .build();
    }
}
