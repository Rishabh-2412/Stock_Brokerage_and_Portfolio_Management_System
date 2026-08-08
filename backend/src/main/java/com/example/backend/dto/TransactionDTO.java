package com.example.backend.dto;

import com.example.backend.entity.enums.TransactionStatus;
import com.example.backend.entity.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response-only - transactions are never created directly from user input,
 * only as the result of executing an order (see
 * POST /api/transactions/execute/{orderId}, which takes no body).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "A completed trade, created by executing an order")
public class TransactionDTO {

    @Schema(example = "101")
    private Long transactionId;

    @Schema(example = "12")
    private Long accountId;

    @Schema(example = "3")
    private Long securityId;

    @Schema(example = "TCS")
    private String symbol;

    @Schema(example = "BUY")
    private TransactionType transactionType;

    @Schema(example = "10")
    private Integer quantity;

    @Schema(example = "3800.00")
    private BigDecimal price;

    @Schema(example = "38000.00")
    private BigDecimal totalAmount;

    @Schema(example = "38.00", description = "Flat 0.1% of totalAmount")
    private BigDecimal commission;

    @Schema(example = "COMPLETED")
    private TransactionStatus status;

    @Schema(example = "2026-08-07T09:20:00")
    private LocalDateTime transactionDate;

    @Schema(example = "2026-08-07T09:20:00", description = "Same as transactionDate - T+0 settlement in this mini project")
    private LocalDateTime settlementDate;
}