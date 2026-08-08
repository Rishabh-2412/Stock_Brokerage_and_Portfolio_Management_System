package com.example.backend.dto;

import com.example.backend.entity.enums.AccountStatus;
import com.example.backend.entity.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Single DTO used for BOTH request and response, per your request.
 *
 * On CREATE (POST /api/accounts): only accountType and balance are read
 * (balance is treated as your initial deposit) - everything else is
 * ignored if you send it.
 *
 * On STATUS UPDATE (PUT /api/accounts/{id}/status): only status is read.
 *
 * On RESPONSE: all fields are populated by the server.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Account data - fields used depend on the endpoint, see per-field notes")
public class AccountDTO {

    @Schema(example = "12", description = "Response only - ignored on create")
    private Long accountId;

    @Schema(example = "7", description = "Response only")
    private Long userId;

    @Schema(example = "ACC10234567", description = "Response only - server generates this")
    private String accountNumber;

    @NotNull(message = "accountType is required")
    @Schema(example = "CASH", description = "Required on create. CASH, MARGIN or DEMO")
    private AccountType accountType;

    @NotNull(message = "balance is required on create (used as initial deposit)")
    @DecimalMin(value = "0.0", inclusive = true, message = "balance cannot be negative")
    @Schema(example = "50000.00", description = "On create: initial deposit amount. On response: current balance")
    private BigDecimal balance;

    @Schema(example = "50000.00", description = "Response only")
    private BigDecimal cashAvailable;

    @Schema(example = "SUSPENDED", description = "Response field. Also the ONLY field read on the status-update endpoint")
    private AccountStatus status;

    @Schema(example = "2026-08-06T10:15:30", description = "Response only")
    private LocalDateTime createdAt;
}