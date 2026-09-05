package com.example.backend.mapper;

import com.example.backend.dto.PortfolioDTO;
import com.example.backend.dto.response.PortfolioResponse;
import com.example.backend.entity.Holdings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PortfolioMapper {

    private PortfolioMapper() {
    }

    /**
     * @param previousClose the security's closing price on the last trading
     *                      day before today (see PriceHistoryRepository
     *                      .findFirstBySecurityIdAndDateLessThanOrderByDateDesc).
     *                      PortfolioServiceImpl passes security.currentPrice
     *                      here instead when no earlier price_history row
     *                      exists, which makes todaysPL correctly come out
     *                      to 0 rather than lying with a fabricated number.
     */
    public static PortfolioDTO toLineDTO(Holdings holding, BigDecimal previousClose) {
        BigDecimal quantity = BigDecimal.valueOf(holding.getQuantity());
        BigDecimal currentPrice = holding.getSecurity().getCurrentPrice();

        BigDecimal costBasis = holding.getAverageCost().multiply(quantity);
        BigDecimal currentValue = currentPrice.multiply(quantity);
        BigDecimal unrealizedPL = currentValue.subtract(costBasis);
        BigDecimal unrealizedPLPercent = costBasis.compareTo(BigDecimal.ZERO) > 0
                ? unrealizedPL.divide(costBasis, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        // Today's P&L: the ACTUAL day's move, using yesterday's close as the
        // baseline - unlike unrealizedPL above, which is measured against
        // purchase price and can be nonzero/zero regardless of what happened today.
        BigDecimal previousValue = previousClose.multiply(quantity);
        BigDecimal todaysPL = currentValue.subtract(previousValue);
        BigDecimal todaysPLPercent = previousValue.compareTo(BigDecimal.ZERO) > 0
                ? todaysPL.divide(previousValue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
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
                .previousClose(previousClose)
                .todaysPL(todaysPL)
                .todaysPLPercent(todaysPLPercent)
                .build();
    }

    /**
     * @param previousCloseBySecurityId lookup built by PortfolioServiceImpl,
     *                                   one entry per distinct security in
     *                                   holdingsList (keyed by security_id).
     */
    public static PortfolioResponse toResponse(
            Long accountId,
            List<Holdings> holdingsList,
            Map<Long, BigDecimal> previousCloseBySecurityId
    ) {
        List<PortfolioDTO> lines = holdingsList.stream()
                .map(h -> toLineDTO(h, previousCloseBySecurityId.get(h.getSecurity().getId())))
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

        BigDecimal totalTodaysPL = lines.stream()
                .map(PortfolioDTO::getTodaysPL)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Yesterday's total portfolio value = sum(previousClose * quantity),
        // reconstructed here from each line so the percentage is weighted
        // correctly across holdings, not just averaged line-by-line.
        BigDecimal totalPreviousValue = lines.stream()
                .map(l -> l.getCurrentValue().subtract(l.getTodaysPL()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTodaysPLPercent = totalPreviousValue.compareTo(BigDecimal.ZERO) > 0
                ? totalTodaysPL.divide(totalPreviousValue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return PortfolioResponse.builder()
                .accountId(accountId)
                .asOf(LocalDateTime.now())
                .totalCostBasis(totalCostBasis)
                .totalCurrentValue(totalCurrentValue)
                .totalUnrealizedPL(totalUnrealizedPL)
                .totalUnrealizedPLPercent(totalUnrealizedPLPercent)
                .totalTodaysPL(totalTodaysPL)
                .totalTodaysPLPercent(totalTodaysPLPercent)
                .holdings(lines)
                .build();
    }
}