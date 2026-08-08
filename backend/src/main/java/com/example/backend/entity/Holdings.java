package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * One row per (account, security) the account currently holds.
 * averageCost is a running weighted average, recalculated on every BUY.
 *
 * currentValue is a SNAPSHOT (quantity * security.currentPrice) written at
 * the moment of the last trade - it will go stale as the security's price
 * moves afterwards with no new trade. The Portfolio module (next) should
 * recompute live P&L from quantity/averageCost + Security.currentPrice
 * directly, not trust this stored field, for anything price-sensitive.
 *
 * NOTE: entity + repository only in this module, per your structure -
 * no HoldingsController/HoldingsService yet. Portfolio module adds those.
 */
@Entity
@Table(
        name = "holdings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "security_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holdings extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    @NotNull
    private Security security;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "average_cost", nullable = false, precision = 19, scale = 4)
    private BigDecimal averageCost;

    @Column(name = "current_value", precision = 19, scale = 4)
    private BigDecimal currentValue;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}