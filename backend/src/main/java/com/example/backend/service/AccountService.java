package com.example.backend.service;

import com.example.backend.dto.AccountDTO;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    /**
     * Create new trading account for user
     * @param userId user identifier
     * @param accountType account type (cash/margin/demo)
     * @return AccountDTO of created account
     */
    AccountDTO createAccount(Long userId, String accountType);

    /**
     * Get account by account ID
     * @param accountId account identifier
     * @return AccountDTO with account details
     */
    AccountDTO getAccountById(Long accountId);

    /**
     * Get all accounts for a user
     * @param userId user identifier
     * @return List of AccountDTOs
     */
    List<AccountDTO> getAccountsByUser(Long userId);

    /**
     * Get account by account number
     * @param accountNumber unique account number
     * @return AccountDTO if found
     */
    AccountDTO getAccountByAccountNumber(String accountNumber);

    /**
     * Get account balance
     * @param accountId account identifier
     * @return current account balance
     */
    BigDecimal getAccountBalance(Long accountId);

    /**
     * Get available cash balance
     * @param accountId account identifier
     * @return cash available for trading
     */
    BigDecimal getCashAvailable(Long accountId);

    /**
     * Deposit funds into account
     * @param accountId account identifier
     * @param amount deposit amount
     * @return updated balance after deposit
     */
    BigDecimal depositFunds(Long accountId, BigDecimal amount);

    /**
     * Withdraw funds from account
     * @param accountId account identifier
     * @param amount withdrawal amount
     * @return updated balance after withdrawal
     */
    BigDecimal withdrawFunds(Long accountId, BigDecimal amount);

    /**
     * Get margin available for margin accounts
     * @param accountId account identifier
     * @return available margin amount
     */
    BigDecimal getAvailableMargin(Long accountId);

    /**
     * Get margin utilization percentage
     * @param accountId account identifier
     * @return margin utilization as percentage
     */
    BigDecimal getMarginUtilizationPercentage(Long accountId);

    /**
     * Update account status
     * @param accountId account identifier
     * @param status new status (active/inactive/suspended)
     */
    void updateAccountStatus(Long accountId, String status);

    /**
     * Get account status
     * @param accountId account identifier
     * @return account status
     */
    String getAccountStatus(Long accountId);

    /**
     * Check if account is active
     * @param accountId account identifier
     * @return true if account is active
     */
    boolean isAccountActive(Long accountId);

    /**
     * Check if account has sufficient balance for transaction
     * @param accountId account identifier
     * @param amount transaction amount
     * @return true if balance is sufficient
     */
    boolean hasSufficientBalance(Long accountId, BigDecimal amount);

    /**
     * Check if account has sufficient margin for trade
     * @param accountId account identifier
     * @param requiredMargin margin required for trade
     * @return true if margin is sufficient
     */
    boolean hasSufficientMargin(Long accountId, BigDecimal requiredMargin);

    /**
     * Get margin call status for account
     * @param accountId account identifier
     * @return true if margin call is active
     */
    boolean hasMarginCall(Long accountId);

    /**
     * Update account balance (after trade execution)
     * @param accountId account identifier
     * @param newBalance new balance amount
     */
    void updateBalance(Long accountId, BigDecimal newBalance);

    /**
     * Update available cash (after trade execution or fund transfer)
     * @param accountId account identifier
     * @param newCash new available cash amount
     */
    void updateCashAvailable(Long accountId, BigDecimal newCash);

    /**
     * Get account type (cash/margin/demo)
     * @param accountId account identifier
     * @return account type
     */
    String getAccountType(Long accountId);
}