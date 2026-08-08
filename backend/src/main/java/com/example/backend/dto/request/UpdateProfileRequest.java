package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Self-service profile update (name/phone only - no email/role change here)")
public class UpdateProfileRequest {

    @NotBlank(message = "fullName is required")
    @Schema(example = "John A. Doe")
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-() ]{7,20}$", message = "phone number is invalid")
    @Schema(example = "+91-9876543210")
    private String phone;
}