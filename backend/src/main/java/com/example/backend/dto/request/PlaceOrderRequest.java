package com.example.backend.dto.request;

import com.example.backend.entity.enums.OrderType;
import com.example.backend.entity.enums.PriceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * price is intentionally NOT @NotNull here - it's required only for LIMIT
 * orders and ignored for MARKET orders. That conditional rule is validated
 * in OrderServiceImpl, not via annotations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to place a new order")
public class PlaceOrderRequest {

    @NotNull(message = "accountId is required")
    @Schema(example = "12", description = "Must be an account you own (CLIENT), or any account (DEALER/ADMIN)")
    private Long accountId;

    @NotNull(message = "securityId is required")
    @Schema(example = "3")
    private Long securityId;

    @NotNull(message = "orderType is required")
    @Schema(example = "BUY", description = "BUY or SELL")
    private OrderType orderType;

    @NotNull(message = "priceType is required")
    @Schema(example = "LIMIT", description = "MARKET or LIMIT")
    private PriceType priceType;

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be positive")
    @Schema(example = "10")
    private Integer quantity;

    @DecimalMin(value = "0.0", inclusive = false, message = "price must be positive")
    @Schema(example = "3800.00", description = "Required if priceType is LIMIT. Ignored if priceType is MARKET")
    private BigDecimal price;
}