package com.example.backend.service;

import com.example.backend.dto.SecurityDTO;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface WatchlistService {

    /**
     * Add security to watchlist
     * @param accountId account identifier
     * @param securityId security identifier
     * @return true if added successfully
     */
    boolean addToWatchlist(Long accountId, Long securityId);

    /**
     * Remove security from watchlist
     * @param accountId account identifier
     * @param securityId security identifier
     * @return true if removed successfully
     */
    boolean removeFromWatchlist(Long accountId, Long securityId);

    /**
     * Get watchlist for account
     * @param accountId account identifier
     * @return List of SecurityDTOs in watchlist
     */
    List<SecurityDTO> getWatchlist(Long accountId);

    /**
     * Get watchlist with current prices and metrics
     * @param accountId account identifier
     * @return List of Map with security data and current prices
     */
    List<Map<String, Object>> getWatchlistWithMetrics(Long accountId);

    /**
     * Check if security is in watchlist
     * @param accountId account identifier
     * @param securityId security identifier
     * @return true if security is in watchlist
     */
    boolean isInWatchlist(Long accountId, Long securityId);

    /**
     * Clear entire watchlist for account
     * @param accountId account identifier
     */
    void clearWatchlist(Long accountId);

    /**
     * Get watchlist size
     * @param accountId account identifier
     * @return number of securities in watchlist
     */
    int getWatchlistSize(Long accountId);

    /**
     * Reorder watchlist (drag-drop functionality)
     * @param accountId account identifier
     * @param securityId security identifier
     * @param newPosition new position in watchlist (1-based)
     */
    void reorderWatchlist(Long accountId, Long securityId, int newPosition);

    /**
     * Add multiple securities to watchlist
     * @param accountId account identifier
     * @param securityIds list of security identifiers
     * @return count of successfully added securities
     */
    int addMultipleToWatchlist(Long accountId, List<Long> securityIds);

    /**
     * Remove multiple securities from watchlist
     * @param accountId account identifier
     * @param securityIds list of security identifiers
     * @return count of successfully removed securities
     */
    int removeMultipleFromWatchlist(Long accountId, List<Long> securityIds);

    /**
     * Export watchlist as CSV/JSON
     * @param accountId account identifier
     * @param format export format (csv/json)
     * @return exported watchlist data
     */
    String exportWatchlist(Long accountId, String format);

    /**
     * Import watchlist from file
     * @param accountId account identifier
     * @param fileContent file content with securities
     * @param format file format (csv/json)
     * @return count of imported securities
     */
    int importWatchlist(Long accountId, String fileContent, String format);

    /**
     * Get watchlist alerts (price threshold notifications)
     * @param accountId account identifier
     * @return Map with watchlist securities and any price alerts
     */
    Map<String, Object> getWatchlistAlerts(Long accountId);

    /**
     * Set price alert for security in watchlist
     * @param accountId account identifier
     * @param securityId security identifier
     * @param upperLimit alert when price goes above this
     * @param lowerLimit alert when price goes below this
     */
    void setPriceAlert(Long accountId, Long securityId, java.math.BigDecimal upperLimit,
                       java.math.BigDecimal lowerLimit);

    /**
     * Remove price alert for security
     * @param accountId account identifier
     * @param securityId security identifier
     */
    void removePriceAlert(Long accountId, Long securityId);

    /**
     * Get watchlist performance summary
     * @param accountId account identifier
     * @return Map with aggregated metrics (top gainers, losers, etc.)
     */
    Map<String, Object> getWatchlistPerformanceSummary(Long accountId);

    /**
     * Sort watchlist by criteria
     * @param accountId account identifier
     * @param sortBy sort criteria (price, change%, sector, name)
     * @param ascending sort direction
     * @return sorted List of SecurityDTOs
     */
    List<SecurityDTO> sortWatchlist(Long accountId, String sortBy, boolean ascending);

    /**
     * Filter watchlist by criteria
     * @param accountId account identifier
     * @param sector sector filter (null for all)
     * @param minPrice minimum price filter
     * @param maxPrice maximum price filter
     * @return filtered List of SecurityDTOs
     */
    List<SecurityDTO> filterWatchlist(Long accountId, String sector, java.math.BigDecimal minPrice,
                                      java.math.BigDecimal maxPrice);

    /**
     * Get added date for watchlist item
     * @param accountId account identifier
     * @param securityId security identifier
     * @return date when security was added to watchlist
     */
    Optional<java.time.LocalDateTime> getAddedDate(Long accountId, Long securityId);
}