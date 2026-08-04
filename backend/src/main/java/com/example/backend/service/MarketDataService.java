package com.example.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MarketDataService {

    /**
     * Get real-time stock quote for security
     * @param symbol stock symbol (ticker)
     * @return Map with quote data (price, bid, ask, volume, etc.)
     */
    Map<String, Object> getStockQuote(String symbol);

    /**
     * Get quotes for multiple symbols
     * @param symbols list of stock symbols
     * @return List of Maps with quote data for each symbol
     */
    List<Map<String, Object>> getMultipleQuotes(List<String> symbols);

    /**
     * Get OHLCV historical chart data
     * @param symbol stock symbol
     * @param startDate start date
     * @param endDate end date
     * @param interval interval (1min, 5min, 15min, 1hour, 1day, 1week, 1month)
     * @return List of OHLCV data points with timestamp
     */
    List<Map<String, Object>> getChartData(String symbol, LocalDate startDate, LocalDate endDate,
                                          String interval);

    /**
     * Get intraday chart data
     * @param symbol stock symbol
     * @param interval time interval for candles
     * @return List of intraday OHLCV data
     */
    List<Map<String, Object>> getIntradayChart(String symbol, String interval);

    /**
     * Get market news for security
     * @param symbol stock symbol
     * @return List of news items with title, date, link
     */
    List<Map<String, String>> getSecurityNews(String symbol);

    /**
     * Get market news (general)
     * @return List of latest market news
     */
    List<Map<String, String>> getMarketNews();

    /**
     * Get market indices data (Nifty, Sensex, etc.)
     * @return Map with current index values and changes
     */
    Map<String, Object> getMarketIndices();

    /**
     * Get specific index data
     * @param indexName index name (Nifty50, Nifty100, SensexIndex, etc.)
     * @return Map with index value and metrics
     */
    Optional<Map<String, Object>> getIndexData(String indexName);

    /**
     * Get market status (open/closed)
     * @return Map with market status and session info
     */
    Map<String, Object> getMarketStatus();

    /**
     * Get market open/close times
     * @return Map with market hours and trading sessions
     */
    Map<String, LocalDateTime> getMarketHours();

    /**
     * Check if market is currently open
     * @return true if market is in trading hours
     */
    boolean isMarketOpen();

    /**
     * Get top gainers for day
     * @param limit number of gainers to return
     * @return List of top gainer securities
     */
    List<Map<String, Object>> getTopGainers(int limit);

    /**
     * Get top losers for day
     * @param limit number of losers to return
     * @return List of top loser securities
     */
    List<Map<String, Object>> getTopLosers(int limit);

    /**
     * Get most active securities by volume
     * @param limit number of securities to return
     * @return List of most active securities
     */
    List<Map<String, Object>> getMostActive(int limit);

    /**
     * Get bid-ask spread for security
     * @param symbol stock symbol
     * @return Map with bid price, ask price, and spread
     */
    Map<String, BigDecimal> getBidAskSpread(String symbol);

    /**
     * Get order book snapshot for security
     * @param symbol stock symbol
     * @return Map with buy/sell orders at different price levels
     */
    Map<String, Object> getOrderBookSnapshot(String symbol);

    /**
     * Refresh real-time price for security
     * @param symbol stock symbol
     * @return latest price
     */
    BigDecimal refreshPrice(String symbol);

    /**
     * Get price alerts (securities breaking key levels)
     * @return List of securities with price alerts
     */
    List<Map<String, Object>> getPriceAlerts();

    /**
     * Subscribe to real-time updates for security (WebSocket)
     * @param symbol stock symbol
     * @param callback callback function to handle price updates
     * @return subscription ID
     */
    String subscribeToPriceUpdates(String symbol, java.util.function.Consumer<Map<String, Object>> callback);

    /**
     * Unsubscribe from real-time updates
     * @param subscriptionId subscription ID to cancel
     */
    void unsubscribeFromPriceUpdates(String subscriptionId);

    /**
     * Get market calendar (trading holidays, etc.)
     * @return List of market events and holidays
     */
    List<Map<String, Object>> getMarketCalendar();

    /**
     * Get upcoming corporate actions (dividends, splits, etc.)
     * @param symbol stock symbol
     * @return List of upcoming corporate actions
     */
     List<Map<String, Object>> getUpcomingCorporateActions(String symbol);

    /**
     * Get technical indicators for security
     * @param symbol stock symbol
     * @param indicator indicator name (RSI, MACD, Bollinger Bands, etc.)
     * @param period period for calculation
     * @return Map with indicator values
     */
    Map<String, Object> getTechnicalIndicator(String symbol, String indicator, int period);

    /**
     * Get moving average
     * @param symbol stock symbol
     * @param period moving average period (20, 50, 200)
     * @return Map with price and moving average value
     */
    Map<String, BigDecimal> getMovingAverage(String symbol, int period);

    /**
     * Get peer comparison data
     * @param symbol stock symbol
     * @return List of peer companies with comparison metrics
     */
    List<Map<String, Object>> getPeerComparison(String symbol);

    /**
     * Get sector performance
     * @param sector sector name
     * @return Map with sector metrics and top companies
     */
    Map<String, Object> getSectorPerformance(String sector);

    /**
     * Sync market data from external source
     * Updates all security prices and market data
     */
    void syncMarketData();

    /**
     * Get last sync timestamp
     * @return timestamp of last market data sync
     */
    LocalDateTime getLastSyncTime();
}