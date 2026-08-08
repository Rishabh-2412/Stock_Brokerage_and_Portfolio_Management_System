package com.example.backend.mapper;

import com.example.backend.dto.PortfolioDTO;
import com.example.backend.dto.response.PortfolioResponse;
import com.example.backend.entity.Holdings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PortfolioMapper {

    private PortfolioMapper() {
    }

    public static PortfolioDTO toLineDTO(Holdings holding) {
        BigDecimal quantity = BigDecimal.valueOf(holding.getQuantity());
        BigDecimal currentPrice = holding.getSecurity().getCurrentPrice();

        BigDecimal costBasis = holding.getAverageCost().multiply(quantity);
        BigDecimal currentValue = currentPrice.multiply(quantity);
        BigDecimal unrealizedPL = currentValue.subtract(costBasis);
        BigDecimal unrealizedPLPercent = costBasis.compareTo(BigDecimal.ZERO) > 0
                ? unrealizedPL.divide(costBasis, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return PortfolioDTO.builder()
                .holdingId(holding.getId())
                .securityId(holding.getSecurity().getId())
                .symbol(holding.getSecurity().getSymbol())
                .securityName(holding.getSecurity().getName())
                .quantity(holding.getQuantity())
                .averageCost(holding.getAverageCost())
                .currentPrice(currentPrice)
                .costBasis(costBasis)
                .currentValue(currentValue)
                .unrealizedPL(unrealizedPL)
                .unrealizedPLPercent(unrealizedPLPercent)
                .build();
    }

    public static PortfolioResponse toResponse(Long accountId, List<Holdings> holdingsList) {
        List<PortfolioDTO> lines = holdingsList.stream()
                .map(PortfolioMapper::toLineDTO)
                .collect(Collectors.toList());

        BigDecimal totalCostBasis = lines.stream()
                .map(PortfolioDTO::getCostBasis)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrentValue = lines.stream()
                .map(PortfolioDTO::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalUnrealizedPL = totalCurrentValue.subtract(totalCostBasis);

        BigDecimal totalUnrealizedPLPercent = totalCostBasis.compareTo(BigDecimal.ZERO) > 0
                ? totalUnrealizedPL.divide(totalCostBasis, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return PortfolioResponse.builder()
                .accountId(accountId)
                .asOf(LocalDateTime.now())
                .totalCostBasis(totalCostBasis)
                .totalCurrentValue(totalCurrentValue)
                .totalUnrealizedPL(totalUnrealizedPL)
                .totalUnrealizedPLPercent(totalUnrealizedPLPercent)
                .holdings(lines)
                .build();
    }
}