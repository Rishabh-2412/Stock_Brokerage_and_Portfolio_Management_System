package com.example.backend.mapper;

import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole())
                .accountType(user.getAccountType())
                .kycStatus(user.getKycStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}