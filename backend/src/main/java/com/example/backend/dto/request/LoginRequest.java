package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login credentials")
public class LoginRequest {

    @NotBlank(message = "username is required")
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "password is required")
    @Schema(example = "Passw0rd!")
    private String password;
}