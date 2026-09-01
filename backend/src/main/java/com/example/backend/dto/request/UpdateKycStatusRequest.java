package com.example.backend.dto.request;

import com.example.backend.entity.enums.KycStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin action to approve or reject a user's KYC status")
public class UpdateKycStatusRequest {

    @NotNull(message = "kycStatus is required")
    @Schema(example = "APPROVED")
    private KycStatus kycStatus;
}