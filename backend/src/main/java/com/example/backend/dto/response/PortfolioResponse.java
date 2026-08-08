package com.example.backend.dto.response;

import com.example.backend.dto.PortfolioDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Full portfolio for one account - a live summary plus each holding's line item")
public class PortfolioResponse {

    @Schema(example = "12")
    private Long accountId;

    @Schema(example = "2026-08-07T10:00:00", description = "When this snapshot was computed - it is NOT cached/stored anywhere")
    private LocalDateTime asOf;

    @Schema(example = "95000.00")
    private BigDecimal totalCostBasis;

    @Schema(example = "97120.00")
    private BigDecimal totalCurrentValue;

    @Schema(example = "2120.00")
    private BigDecimal totalUnrealizedPL;

    @Schema(example = "2.23")
    private BigDecimal totalUnrealizedPLPercent;

    private List<PortfolioDTO> holdings;
}