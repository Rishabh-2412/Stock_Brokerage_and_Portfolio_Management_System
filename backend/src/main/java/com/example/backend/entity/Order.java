package com.example.backend.entity;

import com.example.backend.entity.enums.OrderStatus;
import com.example.backend.entity.enums.OrderType;
import com.example.backend.entity.enums.PriceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * A buy/sell order. NOTE: this entity only represents the order itself -
 * placing one does NOT move money or update Holdings. That happens in the
 * separate Order Execution module, which reads PENDING orders and creates
 * Transactions/updates Holdings/Account cash.
 *
 * The original DB design doc's `order_status` field conflated pricing type
 * (market/limit) with order state (pending/filled/etc). Split here into
 * priceType (MARKET/LIMIT) and orderStatus (PENDING/FILLED/CANCELLED/...).
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    @NotNull
    private Security security;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 10)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false, length = 10)
    private PriceType priceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(nullable = false)
    private Integer quantity;

    /**
     * For LIMIT orders: the price the client set.
     * For MARKET orders: the security's currentPrice at the moment the
     * order was placed, stored for reference/audit - the actual execution
     * price is decided when Module 6 fills the order.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "filled_quantity", nullable = false)
    @Builder.Default
    private Integer filledQuantity = 0;
}