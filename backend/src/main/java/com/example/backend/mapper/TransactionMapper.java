package com.example.backend.mapper;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setAccountId(transaction.getAccountId());
        transactionDTO.setSecurityId(transaction.getSecurityId());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setQuantity(transaction.getQuantity());
        transactionDTO.setPrice(transaction.getPrice());
        transactionDTO.setTotalAmount(transaction.getTotalAmount());
        transactionDTO.setCommission(transaction.getCommission());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setSettlementDate(transaction.getSettlementDate());

        return transactionDTO;
    }

    public Transaction toEntity(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDTO.getTransactionId());
        transaction.setAccountId(transactionDTO.getAccountId());
        transaction.setSecurityId(transactionDTO.getSecurityId());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setQuantity(transactionDTO.getQuantity());
        transaction.setPrice(transactionDTO.getPrice());
        transaction.setTotalAmount(transactionDTO.getTotalAmount());
        transaction.setCommission(transactionDTO.getCommission());
        transaction.setStatus(transactionDTO.getStatus());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setSettlementDate(transactionDTO.getSettlementDate());

        return transaction;
    }
}