package com.example.backend.mapper;

import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setAccountType(user.getAccountType());
        userDTO.setKycStatus(user.getKycStatus());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setAccountType(userDTO.getAccountType());
        user.setKycStatus(userDTO.getKycStatus());

        return user;
    }
}