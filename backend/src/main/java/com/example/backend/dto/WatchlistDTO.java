package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * On ADD (POST /api/watchlist): only accountId and securityId are read.
 * On RESPONSE: symbol/securityName/currentPrice are denormalized from the
 * linked Security, for convenience, so the frontend doesn't need a second
 * call per row.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Watchlist entry - fields used depend on the endpoint, see per-field notes")
public class WatchlistDTO {

    @Schema(example = "5", description = "Response only - ignored on add")
    private Long watchlistId;

    @NotNull(message = "accountId is required")
    @Schema(example = "12", description = "Required on add. Must be an account you own")
    private Long accountId;

    @NotNull(message = "securityId is required")
    @Schema(example = "3", description = "Required on add")
    private Long securityId;

    @Schema(example = "TCS", description = "Response only")
    private String symbol;

    @Schema(example = "Tata Consultancy Services", description = "Response only")
    private String securityName;

    @Schema(example = "3845.50", description = "Response only - current price of the security")
    private BigDecimal currentPrice;

    @Schema(example = "2026-08-06T15:41:51", description = "Response only")
    private LocalDateTime addedAt;
}