package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * One holding line within a Portfolio. Response only - Portfolio is a
 * read-only, computed view (see PortfolioServiceImpl), never created via
 * a request body.
 *
 * currentPrice/currentValue/unrealizedPL are computed LIVE from
 * Security.currentPrice at request time - not read from Holdings'
 * stored (and possibly stale) currentValue field.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "One security's position within an account's portfolio, with live unrealized P&L")
public class PortfolioDTO {

    @Schema(example = "8")
    private Long holdingId;

    @Schema(example = "3")
    private Long securityId;

    @Schema(example = "TCS")
    private String symbol;

    @Schema(example = "Tata Consultancy Services")
    private String securityName;

    @Schema(example = "10")
    private Integer quantity;

    @Schema(example = "3800.00", description = "Weighted average cost per share")
    private BigDecimal averageCost;

    @Schema(example = "3845.50", description = "Security's live current price")
    private BigDecimal currentPrice;

    @Schema(example = "38000.00", description = "quantity * averageCost")
    private BigDecimal costBasis;

    @Schema(example = "38455.00", description = "quantity * currentPrice (live, not the stored snapshot)")
    private BigDecimal currentValue;

    @Schema(example = "455.00", description = "currentValue - costBasis")
    private BigDecimal unrealizedPL;

    @Schema(example = "1.20", description = "unrealizedPL as a percentage of costBasis")
    private BigDecimal unrealizedPLPercent;
}