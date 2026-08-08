package com.example.backend.dto.request;

import com.example.backend.entity.enums.Role;
import com.example.backend.entity.enums.UserCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ADMIN-only: create a user with any role (DEALER, RESEARCH_ANALYST,
 * COMPLIANCE_OFFICER, RISK_MANAGER, another ADMIN, or CLIENT).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin-only request to create a user with any role")
public class CreateUserRequest {

    @NotBlank(message = "username is required")
    @Size(min = 4, max = 50)
    @Schema(example = "dealer_priya")
    private String username;

    @NotBlank(message = "email is required")
    @Email
    @Schema(example = "priya@example.com")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6)
    @Schema(example = "Passw0rd!")
    private String password;

    @NotBlank(message = "fullName is required")
    @Schema(example = "Priya Shah")
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-() ]{7,20}$", message = "phone number is invalid")
    @Schema(example = "+91-9123456780")
    private String phone;

    @NotNull(message = "role is required")
    @Schema(example = "DEALER")
    private Role role;

    @Schema(example = "INDIVIDUAL")
    private UserCategory accountType;
}