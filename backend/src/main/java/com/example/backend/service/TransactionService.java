package com.example.backend.service;

import com.example.backend.dto.TransactionDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    /**
     * Create transaction record after order execution (buy/sell)
     * @param accountId account identifier
     * @param securityId security identifier
     * @param transactionType transaction type (buy/sell/dividend)
     * @param quantity quantity traded
     * @param price execution price
     * @param commission brokerage commission
     * @return TransactionDTO of created transaction
     */
    TransactionDTO createTransaction(Long accountId, Long securityId, String transactionType,
                                    Long quantity, BigDecimal price, BigDecimal commission);

    /**
     * Get transaction by ID
     * @param transactionId transaction identifier
     * @return TransactionDTO with transaction details
     */
    TransactionDTO getTransactionById(Long transactionId);

    /**
     * Get all transactions for an account
     * @param accountId account identifier
     * @return List of TransactionDTOs
     */
    List<TransactionDTO> getTransactionsByAccount(Long accountId);

    /**
     * Get transactions by account with pagination
     * @param accountId account identifier
     * @param pageNumber page index
     * @param pageSize number of records per page
     * @return List of TransactionDTOs
     */
    List<TransactionDTO> getTransactionsByAccount(Long accountId, int pageNumber, int pageSize);

    /**
     * Get transactions by date range
     * @param accountId account identifier
     * @param startDate start date
     * @param endDate end date
     * @return List of TransactionDTOs in date range
     */
    List<TransactionDTO> getTransactionsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);

    /**
     * Get buy transactions for a security
     * @param accountId account identifier
     * @param securityId security identifier
     * @return List of buy TransactionDTOs
     */
    List<TransactionDTO> getBuyTransactions(Long accountId, Long securityId);

    /**
     * Get sell transactions for a security
     * @param accountId account identifier
     * @param securityId security identifier
     * @return List of sell TransactionDTOs
     */
    List<TransactionDTO> getSellTransactions(Long accountId, Long securityId);

    /**
     * Get transaction status
     * @param transactionId transaction identifier
     * @return transaction status (pending/completed/failed/cancelled)
     */
    String getTransactionStatus(Long transactionId);

    /**
     * Update transaction status
     * @param transactionId transaction identifier
     * @param newStatus new transaction status
     */
    void updateTransactionStatus(Long transactionId, String newStatus);

    /**
     * Process settlement for T+1 equity trades
     * @param transactionId transaction identifier
     * SEBI mandate: settlement happens next working day
     */
    void processSettlement(Long transactionId);

    /**
     * Get settlement date for transaction
     * @param transactionId transaction identifier
     * @return settlement date (T+1 working day)
     */
    LocalDate getSettlementDate(Long transactionId);

    /**
     * Record dividend transaction
     * @param accountId account identifier
     * @param securityId security identifier
     * @param dividendAmount dividend amount per unit
     * @param quantity holdings quantity
     * @return TransactionDTO of dividend transaction
     */
    TransactionDTO recordDividendTransaction(Long accountId, Long securityId,
                                            BigDecimal dividendAmount, Long quantity);

    /**
     * Process corporate actions (dividends, bonuses, splits)
     * @param securityId security identifier
     * @param actionType action type (dividend/bonus/split)
     * @param actionValue action value or ratio
     * Processing within 5 working days of record date
     */
    void processCorporateAction(Long securityId, String actionType, BigDecimal actionValue);

    /**
     * Get ledger statement with date filters
     * @param accountId account identifier
     * @param startDate start date
     * @param endDate end date
     * @return List of all transactions for ledger
     */
    List<TransactionDTO> getLedgerStatement(Long accountId, LocalDate startDate, LocalDate endDate);

    /**
     * Calculate total brokerage paid
     * @param accountId account identifier
     * @return sum of all commissions
     */
    BigDecimal getTotalBrokeragePaid(Long accountId);

    /**
     * Calculate total brokerage for period
     * @param accountId account identifier
     * @param startDate start date
     * @param endDate end date
     * @return sum of commissions in period
     */
    BigDecimal getBrokeragePaidForPeriod(Long accountId, LocalDate startDate, LocalDate endDate);

    /**
     * Calculate total buy amount
     * @param accountId account identifier
     * @return sum of all buy transactions (quantity * price)
     */
    BigDecimal getTotalBuyAmount(Long accountId);

    /**
     * Calculate total sell amount
     * @param accountId account identifier
     * @return sum of all sell transactions (quantity * price)
     */
    BigDecimal getTotalSellAmount(Long accountId);

    /**
     * Get transaction history for reporting/audit
     * @param accountId account identifier
     * @return List of all transactions for audit trail
     */
    List<TransactionDTO> getTransactionHistoryForAudit(Long accountId);

    /**
     * Trigger short delivery settlement per NSE/BSE guidelines
     * @param transactionId transaction identifier
     */
    void triggerShortDeliverySettlement(Long transactionId);

    /**
     * Submit SEBI daily activity report
     * @return true if submission successful
     * Report submitted by end of trading day
     */
    boolean submitSebiDailyActivityReport();
}