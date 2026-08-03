package com.example.backend.service.validator;

import com.example.backend.entity.Account;
import com.example.backend.entity.Holdings;
import com.example.backend.entity.Transaction;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.InsufficientMarginException;
import org.springframework.stereotype.Component;

@Component
public class TradeValidator {

    public void validateBuyTrade(Transaction transaction, Account account, Double totalAmount) {
        if (transaction.getQuantity() <= 0) {
            throw new InvalidOrderException("Trade quantity must be greater than 0");
        }
        if (transaction.getPrice() <= 0) {
            throw new InvalidOrderException("Trade price must be greater than 0");
        }
        if (account.getCashAvailable() < totalAmount) {
            throw new InsufficientMarginException("Insufficient funds for buy trade");
        }
    }

    public void validateSellTrade(Transaction transaction, Holdings holdings) {
        if (transaction.getQuantity() <= 0) {
            throw new InvalidOrderException("Trade quantity must be greater than 0");
        }
        if (holdings == null || holdings.getQuantity() < transaction.getQuantity()) {
            throw new InvalidOrderException("Insufficient holdings to sell");
        }
        if (transaction.getPrice() <= 0) {
            throw new InvalidOrderException("Trade price must be greater than 0");
        }
    }

    public void validateTransactionAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidOrderException("Transaction amount must be greater than 0");
        }
    }

    public void validateTransactionType(String transactionType) {
        if (!transactionType.equals("buy") && !transactionType.equals("sell") && 
            !transactionType.equals("dividend")) {
            throw new InvalidOrderException("Invalid transaction type");
        }
    }

    public void validateTransactionStatus(String status) {
        if (!status.equals("pending") && !status.equals("completed") && 
            !status.equals("failed") && !status.equals("cancelled")) {
            throw new InvalidOrderException("Invalid transaction status");
        }
    }

    public void validateCommission(Double commission) {
        if (commission < 0) {
            throw new InvalidOrderException("Commission cannot be negative");
        }
    }
}