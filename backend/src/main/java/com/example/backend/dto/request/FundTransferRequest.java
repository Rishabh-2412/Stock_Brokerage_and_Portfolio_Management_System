package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to deposit or withdraw funds from a trading account")
public class FundTransferRequest {

    @NotNull(message = "Account ID is required")
    @Schema(example = "12", description = "Target account ID")
    private Long accountId;

    @NotBlank(message = "Transfer type is required")
    @Schema(example = "DEPOSIT", description = "Allowed values: DEPOSIT or WITHDRAWAL")
    private String transferType; // deposit or withdrawal

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(example = "5000.00", description = "Amount to deposit or withdraw")
    private BigDecimal amount;

    @NotBlank(message = "Description is required")
    @Schema(example = "Bank transfer top-up", description = "Reason or notes for the transfer")
    private String description;

    @Schema(example = "BANK-001", description = "Optional bank account reference")
    private String bankAccountNumber;

    @Schema(example = "REF-20260829-001", description = "Optional external transfer reference")
    private String transactionReference;
}