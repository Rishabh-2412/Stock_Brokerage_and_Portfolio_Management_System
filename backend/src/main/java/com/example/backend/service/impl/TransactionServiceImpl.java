package com.example.backend.service.impl;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.Security;
import com.example.backend.entity.Transaction;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.TransactionMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public TransactionDTO createTransaction(Long accountId, Long securityId, String type, 
                                           Long quantity, Double price, Double commission) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setSecurity(security);
        transaction.setTransactionType(type);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(quantity * price);
        transaction.setCommission(commission);
        transaction.setStatus("pending");
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDTO(savedTransaction);
    }

    @Override
    public TransactionDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        return transactionMapper.toDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccount(account).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByType(Long accountId, String type) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccountAndTransactionType(account, type).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByStatus(Long accountId, String status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccountAndStatus(account, status).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO updateTransactionStatus(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        transaction.setStatus(status);
        
        if ("completed".equalsIgnoreCase(status)) {
            transaction.setSettlementDate(LocalDateTime.now().plusDays(2));
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDTO(updatedTransaction);
    }

    @Override
    public List<TransactionDTO> getTransactionHistory(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccount(account).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalCommissions(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccount(account).stream()
                .mapToDouble(Transaction::getCommission)
                .sum();
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        transactionRepository.delete(transaction);
    }
}