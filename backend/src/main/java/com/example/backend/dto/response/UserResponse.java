package com.example.backend.dto.response;

import com.example.backend.entity.enums.KycStatus;
import com.example.backend.entity.enums.Role;
import com.example.backend.entity.enums.UserCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User profile returned by the API")
public class UserResponse {

    @Schema(example = "7")
    private Long userId;

    @Schema(example = "john_doe")
    private String username;

    @Schema(example = "john@example.com")
    private String email;

    @Schema(example = "John Doe")
    private String fullName;

    @Schema(example = "+91-9876543210")
    private String phone;

    @Schema(example = "CLIENT")
    private Role role;

    @Schema(example = "INDIVIDUAL")
    private UserCategory accountType;

    @Schema(example = "APPROVED")
    private KycStatus kycStatus;

    @Schema(example = "2026-08-06T10:15:30")
    private LocalDateTime createdAt;
}