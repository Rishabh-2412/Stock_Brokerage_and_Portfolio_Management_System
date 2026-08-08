package com.example.backend.mapper;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.entity.Transaction;

public class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return TransactionDTO.builder()
                .transactionId(transaction.getId())
                .accountId(transaction.getAccount() != null ? transaction.getAccount().getId() : null)
                .securityId(transaction.getSecurity() != null ? transaction.getSecurity().getId() : null)
                .symbol(transaction.getSecurity() != null ? transaction.getSecurity().getSymbol() : null)
                .transactionType(transaction.getTransactionType())
                .quantity(transaction.getQuantity())
                .price(transaction.getPrice())
                .totalAmount(transaction.getTotalAmount())
                .commission(transaction.getCommission())
                .status(transaction.getStatus())
                .transactionDate(transaction.getTransactionDate())
                .settlementDate(transaction.getSettlementDate())
                .build();
    }
}