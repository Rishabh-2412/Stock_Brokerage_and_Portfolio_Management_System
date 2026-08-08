package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tradable instrument (stock). Matches the Securities table in the DB
 * design doc. `lastUpdated` is separate from BaseEntity's inherited
 * `updatedAt` - it specifically tracks when the PRICE was last changed,
 * since that's what other modules (Orders, Holdings, Watchlist) will
 * actually read.
 */
@Entity
@Table(name = "securities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Security extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 50)
    private String exchange;

    @Column(length = 50)
    private String sector;

    @Column(name = "current_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal currentPrice;

    @Column(name = "market_cap", precision = 19, scale = 2)
    private BigDecimal marketCap;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}