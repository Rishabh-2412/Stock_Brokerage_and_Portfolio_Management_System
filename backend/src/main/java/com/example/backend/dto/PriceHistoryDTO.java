package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Single DTO for both ADD (ADMIN) and RESPONSE, matching the pattern used
 * elsewhere in this project. On add, historyId and symbol are ignored.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "One day's OHLCV record for a security")
public class PriceHistoryDTO {

    @Schema(example = "77", description = "Response only - ignored on add")
    private Long historyId;

    @NotNull(message = "securityId is required")
    @Schema(example = "3")
    private Long securityId;

    @Schema(example = "TCS", description = "Response only")
    private String symbol;

    @NotNull(message = "openPrice is required")
    @PositiveOrZero
    @Schema(example = "3800.00")
    private BigDecimal openPrice;

    @NotNull(message = "highPrice is required")
    @PositiveOrZero
    @Schema(example = "3860.00")
    private BigDecimal highPrice;

    @NotNull(message = "lowPrice is required")
    @PositiveOrZero
    @Schema(example = "3790.00")
    private BigDecimal lowPrice;

    @NotNull(message = "closePrice is required")
    @PositiveOrZero
    @Schema(example = "3845.50")
    private BigDecimal closePrice;

    @NotNull(message = "volume is required")
    @PositiveOrZero
    @Schema(example = "1250000")
    private Long volume;

    @NotNull(message = "date is required")
    @Schema(example = "2026-08-06")
    private LocalDate date;
}