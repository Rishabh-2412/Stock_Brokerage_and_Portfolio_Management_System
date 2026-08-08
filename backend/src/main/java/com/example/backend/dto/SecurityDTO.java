package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Single DTO used across create / price-update / response, per your request.
 *
 * On CREATE (POST /api/securities): symbol, name, currentPrice are required;
 * exchange, sector, marketCap optional. securityId/lastUpdated ignored.
 *
 * On PRICE UPDATE (PUT /api/securities/{id}/price): only currentPrice
 * (required) and marketCap (optional) are read - everything else ignored.
 *
 * On RESPONSE: all fields populated by the server.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Security data - fields used depend on the endpoint, see per-field notes")
public class SecurityDTO {

    @Schema(example = "3", description = "Response only - ignored on create/update")
    private Long securityId;

    @NotBlank(message = "symbol is required")
    @Schema(example = "TCS", description = "Required on create. Ignored on price update")
    private String symbol;

    @NotBlank(message = "name is required")
    @Schema(example = "Tata Consultancy Services", description = "Required on create. Ignored on price update")
    private String name;

    @Schema(example = "NSE", description = "Optional on create. Ignored on price update")
    private String exchange;

    @Schema(example = "IT Services", description = "Optional on create. Ignored on price update")
    private String sector;

    @NotNull(message = "currentPrice is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "currentPrice must be positive")
    @Schema(example = "3845.50", description = "Required on create AND on price update")
    private BigDecimal currentPrice;

    @DecimalMin(value = "0.0", inclusive = true, message = "marketCap cannot be negative")
    @Schema(example = "1390000000000.00", description = "Optional on create and price update")
    private BigDecimal marketCap;

    @Schema(example = "2026-08-06T15:41:51", description = "Response only")
    private LocalDateTime lastUpdated;
}