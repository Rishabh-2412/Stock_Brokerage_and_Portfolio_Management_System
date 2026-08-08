package com.example.backend.service;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.entity.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransactionDTO executeOrder(String username, Long orderId);

    TransactionDTO getTransactionById(String username, Long transactionId);

    /**
     * type/startDate/endDate are all optional (pass null to skip a filter).
     * startDate/endDate are inclusive, compared against transactionDate.
     */
    List<TransactionDTO> getTransactionsForAccount(String username, Long accountId,
                                                     TransactionType type,
                                                     LocalDate startDate,
                                                     LocalDate endDate);

    List<TransactionDTO> getAllTransactions();
}