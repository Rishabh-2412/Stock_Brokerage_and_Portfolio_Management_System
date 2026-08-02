package com.example.backend.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.example.backend.entity.Transaction;
import com.example.backend.entity.Account;
import com.example.backend.entity.Security;
import com.example.backend.entity.Holdings;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.HoldingsRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.util.AuditLogger;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Component
public class TransactionListener {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HoldingsRepository holdingsRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private AuditLogger auditLogger;

    @EventListener
    public void onTransactionCreated(TransactionCreatedEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();
        
        try {
            transaction.setStatus("pending");
            transactionRepository.save(transaction);
            auditLogger.log("Transaction created", "TRANSACTION_CREATED", transaction.getTransactionId(), account.getUserId());
        } catch (Exception e) {
            auditLogger.log("Transaction creation failed: " + e.getMessage(), "TRANSACTION_CREATION_ERROR", 
                          transaction.getTransactionId(), account.getUserId());
            throw new RuntimeException("Transaction creation failed", e);
        }
    }

    @EventListener
    public void onTransactionCompleted(TransactionCompletedEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();
        Security security = transaction.getSecurity();
        
        try {
            if ("BUY".equalsIgnoreCase(transaction.getTransactionType())) {
                handleBuyTransaction(transaction, account, security);
            } else if ("SELL".equalsIgnoreCase(transaction.getTransactionType())) {
                handleSellTransaction(transaction, account, security);
            } else if ("DIVIDEND".equalsIgnoreCase(transaction.getTransactionType())) {
                handleDividendTransaction(transaction, account);
            }
            
            transaction.setStatus("completed");
            transactionRepository.save(transaction);
            auditLogger.log("Transaction completed successfully", "TRANSACTION_COMPLETED", 
                          transaction.getTransactionId(), account.getUserId());
            
        } catch (Exception e) {
            transaction.setStatus("failed");
            transactionRepository.save(transaction);
            auditLogger.log("Transaction completion failed: " + e.getMessage(), "TRANSACTION_COMPLETION_ERROR", 
                          transaction.getTransactionId(), account.getUserId());
            throw new RuntimeException("Transaction completion failed", e);
        }
    }

    private void handleBuyTransaction(Transaction transaction, Account account, Security security) {
        BigDecimal totalCost = transaction.getTotalAmount().add(transaction.getCommission());

        // Verify sufficient funds
        if (account.getCashAvailable().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient funds to complete buy transaction");
        }

        // Update account balance
        account.setCashAvailable(account.getCashAvailable().subtract(totalCost));
        account.setBalance(account.getBalance().subtract(totalCost));
        accountRepository.save(account);

        // Update holdings
        Holdings holdings = holdingsRepository.findByAccountAndSecurity(account, security);
        if (holdings != null) {
            BigDecimal totalValue = holdings.getCurrentValue().add(transaction.getTotalAmount());
            long newQuantity = holdings.getQuantity() + transaction.getQuantity();
            BigDecimal newAverageCost = totalValue.divide(new BigDecimal(newQuantity), 2, BigDecimal.ROUND_HALF_UP);
            
            holdings.setQuantity(newQuantity);
            holdings.setAverageCost(newAverageCost);
            holdings.setCurrentValue(totalValue);
            holdings.setLastUpdated(LocalDateTime.now());
        } else {
            holdings = new Holdings();
            holdings.setAccount(account);
            holdings.setSecurity(security);
            holdings.setQuantity(transaction.getQuantity());
            holdings.setAverageCost(transaction.getPrice());
            holdings.setCurrentValue(transaction.getTotalAmount());
            holdings.setLastUpdated(LocalDateTime.now());
        }
        holdingsRepository.save(holdings);
    }

    private void handleSellTransaction(Transaction transaction, Account account, Security security) {
        Holdings holdings = holdingsRepository.findByAccountAndSecurity(account, security);
        
        // Verify sufficient holdings
        if (holdings == null || holdings.getQuantity() < transaction.getQuantity()) {
            throw new RuntimeException("Insufficient holdings to complete sell transaction");
        }

        BigDecimal netProceeds = transaction.getTotalAmount().subtract(transaction.getCommission());

        // Update account balance
        account.setCashAvailable(account.getCashAvailable().add(netProceeds));
        account.setBalance(account.getBalance().add(netProceeds));
        accountRepository.save(account);

        // Update holdings
        holdings.setQuantity(holdings.getQuantity() - transaction.getQuantity());
        holdings.setCurrentValue(holdings.getCurrentValue().subtract(transaction.getTotalAmount()));
        holdings.setLastUpdated(LocalDateTime.now());
        
        if (holdings.getQuantity() == 0) {
            holdingsRepository.delete(holdings);
        } else {
            holdingsRepository.save(holdings);
        }
    }

    private void handleDividendTransaction(Transaction transaction, Account account) {
        // Add dividend proceeds to cash available
        account.setCashAvailable(account.getCashAvailable().add(transaction.getTotalAmount()));
        account.setBalance(account.getBalance().add(transaction.getTotalAmount()));
        accountRepository.save(account);
    }

    @EventListener
    public void onTransactionFailed(TransactionFailedEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();
        String reason = event.getReason();
        
        try {
            transaction.setStatus("failed");
            transactionRepository.save(transaction);
            auditLogger.log("Transaction failed: " + reason, "TRANSACTION_FAILED", 
                          transaction.getTransactionId(), account.getUserId());
        } catch (Exception e) {
            auditLogger.log("Error handling transaction failure: " + e.getMessage(), "TRANSACTION_FAILURE_HANDLING_ERROR", 
                          transaction.getTransactionId(), account.getUserId());
            throw new RuntimeException("Error handling transaction failure", e);
        }
    }

    @EventListener
    public void onTransactionCancelled(TransactionCancelledEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();
        
        try {
            // If transaction was pending and involved funds, reverse the reservation
            if ("pending".equalsIgnoreCase(transaction.getStatus())) {
                if ("BUY".equalsIgnoreCase(transaction.getTransactionType())) {
                    BigDecimal reversedAmount = transaction.getTotalAmount().add(transaction.getCommission());
                    account.setCashAvailable(account.getCashAvailable().add(reversedAmount));
                    accountRepository.save(account);
                }
            }
            
            transaction.setStatus("cancelled");
            transactionRepository.save(transaction);
            auditLogger.log("Transaction cancelled", "TRANSACTION_CANCELLED", 
                          transaction.getTransactionId(), account.getUserId());
        } catch (Exception e) {
            auditLogger.log("Error cancelling transaction: " + e.getMessage(), "TRANSACTION_CANCELLATION_ERROR", 
                          transaction.getTransactionId(), account.getUserId());
            throw new RuntimeException("Error cancelling transaction", e);
        }
    }

    @EventListener
    public void onTransactionSettled(TransactionSettledEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();
        
        try {
            transaction.setSettlementDate(LocalDateTime.now());
            transactionRepository.save(transaction);
            auditLogger.log("Transaction settled", "TRANSACTION_SETTLED", 
                          transaction.getTransactionId(), account.getUserId());
        } catch (Exception e) {
            auditLogger.log("Error settling transaction: " + e.getMessage(), "TRANSACTION_SETTLEMENT_ERROR", 
                          transaction.getTransactionId(), account.getUserId());
            throw new RuntimeException("Error settling transaction", e);
        }
    }
}