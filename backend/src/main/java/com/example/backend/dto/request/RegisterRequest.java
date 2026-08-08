package com.example.backend.dto.request;

import com.example.backend.entity.enums.UserCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Public self-registration. Note there is deliberately NO role field here -
 * every self-registered user becomes CLIENT. Other roles are created only
 * by an ADMIN via POST /api/users (see CreateUserRequest).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "New user (CLIENT) self-registration request")
public class RegisterRequest {

    @NotBlank(message = "username is required")
    @Size(min = 4, max = 50, message = "username must be 4-50 characters")
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Schema(example = "john@example.com")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    @Schema(example = "Passw0rd!")
    private String password;

    @NotBlank(message = "fullName is required")
    @Schema(example = "John Doe")
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-() ]{7,20}$", message = "phone number is invalid")
    @Schema(example = "+91-9876543210")
    private String phone;

    @Schema(example = "INDIVIDUAL", description = "INDIVIDUAL or INSTITUTIONAL, defaults to INDIVIDUAL")
    private UserCategory accountType;
}