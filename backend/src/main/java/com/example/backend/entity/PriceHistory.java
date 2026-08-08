package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * One daily OHLCV (open/high/low/close/volume) record for a Security.
 * Populated manually by ADMIN in this mini project (see MarketController) -
 * a real system would populate this from an exchange feed on a schedule.
 */
@Entity
@Table(
        name = "price_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"security_id", "date"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    @NotNull
    private Security security;

    @Column(name = "open_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal openPrice;

    @Column(name = "high_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal highPrice;

    @Column(name = "low_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal lowPrice;

    @Column(name = "close_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal closePrice;

    @Column(nullable = false)
    private Long volume;

    @Column(nullable = false)
    private LocalDate date;
}