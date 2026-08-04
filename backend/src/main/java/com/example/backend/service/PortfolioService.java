package com.example.backend.service;

import com.example.backend.dto.PortfolioDTO;
import com.example.backend.dto.response.PortfolioResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PortfolioService {

    /**
     * Get complete portfolio with holdings and real-time MTM P&L
     * @param accountId account identifier
     * @return PortfolioDTO with all holdings and calculated values
     */
    PortfolioDTO getPortfolio(Long accountId);

    /**
     * Get portfolio as API response format
     * @param accountId account identifier
     * @return PortfolioResponse with formatted data
     */
    PortfolioResponse getPortfolioResponse(Long accountId);

    /**
     * Get portfolio performance metrics (returns, analytics)
     * @param accountId account identifier
     * @return Map containing performance metrics
     */
    Map<String, Object> getPortfolioPerformance(Long accountId);

    /**
     * Get total portfolio value (current)
     * @param accountId account identifier
     * @return total portfolio value
     */
    BigDecimal getTotalPortfolioValue(Long accountId);

    /**
     * Get total investment cost (average cost * quantity sum)
     * @param accountId account identifier
     * @return total invested amount
     */
    BigDecimal getTotalInvestmentCost(Long accountId);

    /**
     * Get total current value of all holdings
     * @param accountId account identifier
     * @return total current value
     */
    BigDecimal getTotalCurrentValue(Long accountId);

    /**
     * Calculate unrealized profit/loss (MTM)
     * @param accountId account identifier
     * @return unrealized P&L
     */
    BigDecimal getUnrealizedPnl(Long accountId);

    /**
     * Calculate realized profit/loss from executed trades
     * @param accountId account identifier
     * @return realized P&L
     */
    BigDecimal getRealizedPnl(Long accountId);

    /**
     * Calculate total P&L (realized + unrealized)
     * @param accountId account identifier
     * @return total P&L
     */
    BigDecimal getTotalPnl(Long accountId);

    /**
     * Get return on investment percentage
     * @param accountId account identifier
     * @return ROI as percentage
     */
    BigDecimal getReturnOnInvestment(Long accountId);

    /**
     * Get portfolio allocation by sector
     * @param accountId account identifier
     * @return Map with sector names and allocation percentages
     */
    Map<String, BigDecimal> getPortfolioAllocationBySector(Long accountId);

    /**
     * Get portfolio allocation by security type
     * @param accountId account identifier
     * @return Map with security types and allocation percentages
     */
    Map<String, BigDecimal> getPortfolioAllocationByType(Long accountId);

    /**
     * Get individual holding details
     * @param holdingId holding identifier
     * @return Map with holding details and calculated values
     */
    Map<String, Object> getHoldingDetails(Long holdingId);

    /**
     * Update holding current value (after market price update)
     * @param holdingId holding identifier
     * @param currentPrice current market price
     */
    void updateHoldingValue(Long holdingId, BigDecimal currentPrice);

    /**
     * Get tax loss harvesting report for year
     * @param accountId account identifier
     * @param year calendar year
     * @return Map with tax loss details and annual P&L
     */
    Map<String, Object> getTaxReport(Long accountId, int year);

    /**
     * Get annual tax P&L report with detailed breakdown
     * @param accountId account identifier
     * @param year calendar year
     * @return detailed tax report data
     */
    PortfolioResponse getTaxPnlReport(Long accountId, int year);

    /**
     * Get portfolio risk metrics (beta, standard deviation, sharpe ratio)
     * @param accountId account identifier
     * @return Map with risk metrics
     */
    Map<String, BigDecimal> getRiskMetrics(Long accountId);

    /**
     * Calculate portfolio beta
     * @param accountId account identifier
     * @return portfolio beta value
     */
    BigDecimal getPortfolioBeta(Long accountId);

    /**
     * Get dividend income for period
     * @param accountId account identifier
     * @param startDate start date
     * @param endDate end date
     * @return total dividend income
     */
    BigDecimal getDividendIncome(Long accountId, java.time.LocalDate startDate, java.time.LocalDate endDate);

    /**
     * Recalculate all portfolio values (called when prices update)
     * @param accountId account identifier
     */
    void recalculatePortfolioValues(Long accountId);

    /**
     * Get portfolio summary for dashboard
     * @param accountId account identifier
     * @return Map with key metrics (total value, P&L, ROI, cash)
     */
    Map<String, Object> getPortfolioSummary(Long accountId);
}