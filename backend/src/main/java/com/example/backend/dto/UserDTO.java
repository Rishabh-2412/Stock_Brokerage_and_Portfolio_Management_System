package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDateTime;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
 
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String accountType;
    private String kycStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
 
}