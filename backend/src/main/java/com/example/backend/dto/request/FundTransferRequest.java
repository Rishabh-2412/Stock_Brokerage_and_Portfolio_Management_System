package com.example.backend.dto.request;

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
public class FundTransferRequest {
 
    @NotNull(message = "Account ID is required")
    private Long accountId;
 
    @NotBlank(message = "Transfer type is required")
    private String transferType; // deposit or withdrawal
 
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
 
    @NotBlank(message = "Description is required")
    private String description;
 
    private String bankAccountNumber; // For deposit/withdrawal reference
 
    private String transactionReference; // External reference ID from bank
 
}