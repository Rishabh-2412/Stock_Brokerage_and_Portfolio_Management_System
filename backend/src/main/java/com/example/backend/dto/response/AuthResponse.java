package com.example.backend.dto.response;

import com.example.backend.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Returned on successful login")
public class AuthResponse {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Builder.Default
    @Schema(example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(example = "7")
    private Long userId;

    @Schema(example = "john_doe")
    private String username;

    @Schema(example = "CLIENT")
    private Role role;
}