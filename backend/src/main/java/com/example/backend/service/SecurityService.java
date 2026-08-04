package com.example.backend.service;

import com.example.backend.dto.SecurityDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface SecurityService {

    /**
     * Get security by ID
     * @param securityId security identifier
     * @return SecurityDTO with security details
     */
    SecurityDTO getSecurityById(Long securityId);

    /**
     * Get security by symbol (ticker)
     * @param symbol stock symbol (e.g., INFY, TCS)
     * @return Optional containing SecurityDTO
     */
    Optional<SecurityDTO> getSecurityBySymbol(String symbol);

    /**
     * Get all securities (stocks/instruments)
     * @return List of all available SecurityDTOs
     */
    List<SecurityDTO> getAllSecurities();

    /**
     * Get securities by sector
     * @param sector sector name (e.g., IT, Finance, Energy)
     * @return List of SecurityDTOs in that sector
     */
    List<SecurityDTO> getSecuritiesBySector(String sector);

    /**
     * Get securities by exchange
     * @param exchange exchange name (NSE, BSE)
     * @return List of SecurityDTOs on that exchange
     */
    List<SecurityDTO> getSecuritiesByExchange(String exchange);

    /**
     * Search securities by name pattern
     * @param searchTerm partial company name
     * @return List of matching SecurityDTOs
     */
    List<SecurityDTO> searchSecurities(String searchTerm);

    /**
     * Get current price for security
     * @param securityId security identifier
     * @return current market price
     */
    BigDecimal getCurrentPrice(Long securityId);

    /**
     * Update security current price
     * @param securityId security identifier
     * @param newPrice new market price
     */
    void updateCurrentPrice(Long securityId, BigDecimal newPrice);

    /**
     * Get security market cap
     * @param securityId security identifier
     * @return market capitalization
     */
    BigDecimal getMarketCap(Long securityId);

    /**
     * Get price history for security
     * @param securityId security identifier
     * @param days number of historical days to retrieve
     * @return List of daily price records (OHLCV data)
     */
    List<Map<String, Object>> getPriceHistory(Long securityId, int days);

    /**
     * Get OHLCV data for chart
     * @param securityId security identifier
     * @param startDate start date
     * @param endDate end date
     * @return List with OHLCV (Open, High, Low, Close, Volume) data points
     */
    List<Map<String, Object>> getOhlcvData(Long securityId, LocalDate startDate, LocalDate endDate);

    /**
     * Record daily price update
     * @param securityId security identifier
     * @param openPrice opening price
     * @param highPrice highest price of day
     * @param lowPrice lowest price of day
     * @param closePrice closing price
     * @param volume trading volume
     */
    void recordDailyPrice(Long securityId, BigDecimal openPrice, BigDecimal highPrice,
                         BigDecimal lowPrice, BigDecimal closePrice, Long volume);

    /**
     * Get sector list (for filters)
     * @return List of unique sector names
     */
    List<String> getAllSectors();

    /**
     * Get exchange list (for filters)
     * @return List of unique exchange names
     */
    List<String> getAllExchanges();

    /**
     * Check if security exists
     * @param symbol stock symbol
     * @return true if security exists in database
     */
    boolean securityExists(String symbol);

    /**
     * Add new security to database
     * @param securityDTO security details
     * @return created SecurityDTO
     */
    SecurityDTO addSecurity(SecurityDTO securityDTO);

    /**
     * Update security information
     * @param securityId security identifier
     * @param securityDTO updated security data
     * @return updated SecurityDTO
     */
    SecurityDTO updateSecurity(Long securityId, SecurityDTO securityDTO);

    /**
     * Get trading volume for security
     * @param securityId security identifier
     * @return current trading volume
     */
    Long getTradingVolume(Long securityId);

    /**
     * Get price change for day
     * @param securityId security identifier
     * @return price change amount
     */
    BigDecimal getDayPriceChange(Long securityId);

    /**
     * Get price change percentage for day
     * @param securityId security identifier
     * @return price change as percentage
     */
    BigDecimal getDayPriceChangePercentage(Long securityId);

    /**
     * Get 52-week high
     * @param securityId security identifier
     * @return 52-week high price
     */
    BigDecimal get52WeekHigh(Long securityId);

    /**
     * Get 52-week low
     * @param securityId security identifier
     * @return 52-week low price
     */
    BigDecimal get52WeekLow(Long securityId);

    /**
     * Update all securities prices (batch update after market data)
     * @param priceUpdates Map of security symbols to new prices
     */
    void updatePricesBatch(Map<String, BigDecimal> priceUpdates);

    /**
     * Get last updated timestamp for security
     * @param securityId security identifier
     * @return last price update datetime
     */
    java.time.LocalDateTime getLastUpdated(Long securityId);
}