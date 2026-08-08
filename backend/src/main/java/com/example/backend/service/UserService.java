package com.example.backend.service;

import com.example.backend.dto.request.CreateUserRequest;
import com.example.backend.dto.request.UpdateProfileRequest;
import com.example.backend.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getMyProfile(String username);

    UserResponse updateMyProfile(String username, UpdateProfileRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    UserResponse createUser(CreateUserRequest request);
}