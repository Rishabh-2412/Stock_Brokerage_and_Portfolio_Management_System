package com.example.backend.service.impl;

import com.example.backend.dto.request.CreateUserRequest;
import com.example.backend.dto.request.UpdateProfileRequest;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.entity.enums.KycStatus;
import com.example.backend.entity.enums.UserCategory;
import com.example.backend.exception.DuplicateTradeException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyProfile(String username) {
        return UserMapper.toResponse(getUserByUsernameOrThrow(username));
    }

    @Override
    public UserResponse updateMyProfile(String username, UpdateProfileRequest request) {
        User user = getUserByUsernameOrThrow(username);
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateTradeException("Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateTradeException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(request.getRole())
                .accountType(request.getAccountType() != null ? request.getAccountType() : UserCategory.INDIVIDUAL)
                .kycStatus(KycStatus.APPROVED)
                .build();

        return UserMapper.toResponse(userRepository.save(user));
    }

    private User getUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}