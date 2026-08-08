package com.example.backend.dto.response;

import com.example.backend.entity.enums.OrderStatus;
import com.example.backend.entity.enums.OrderType;
import com.example.backend.entity.enums.PriceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order details returned to the client")
public class OrderResponse {

    @Schema(example = "45")
    private Long orderId;

    @Schema(example = "12")
    private Long accountId;

    @Schema(example = "3")
    private Long securityId;

    @Schema(example = "TCS", description = "Denormalized from the linked Security")
    private String symbol;

    @Schema(example = "BUY")
    private OrderType orderType;

    @Schema(example = "LIMIT")
    private PriceType priceType;

    @Schema(example = "PENDING")
    private OrderStatus orderStatus;

    @Schema(example = "10")
    private Integer quantity;

    @Schema(example = "3800.00", description = "LIMIT price you set, or the security's price at the moment a MARKET order was placed")
    private BigDecimal price;

    @Schema(example = "0", description = "How much of the order has been filled so far - always 0 until Module 6 (execution) is built")
    private Integer filledQuantity;

    @Schema(example = "2026-08-07T09:12:00")
    private LocalDateTime createdAt;

    @Schema(example = "2026-08-07T09:12:00")
    private LocalDateTime updatedAt;
}