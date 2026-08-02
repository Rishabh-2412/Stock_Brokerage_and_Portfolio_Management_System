package com.example.backend.controller;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/type/{transactionType}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByType(@PathVariable Long accountId,
            @PathVariable String transactionType) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByType(accountId, transactionType);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/date-range")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(@PathVariable Long accountId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(accountId, startDate,
                endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/security/{securityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getTransactionsBySecurity(@PathVariable Long accountId,
            @PathVariable Long securityId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsBySecurityAndAccount(accountId,
                securityId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/pending")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getPendingTransactions(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getPendingTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/completed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getCompletedTransactions(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getCompletedTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/failed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> getFailedTransactions(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getFailedTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{transactionId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionDTO> updateTransactionStatus(@PathVariable Long transactionId,
            @RequestParam String status) {
        TransactionDTO transaction = transactionService.updateTransactionStatus(transactionId, status);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/account/{accountId}/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getTransactionSummary(@PathVariable Long accountId) {
        Object summary = transactionService.getTransactionSummary(accountId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/account/{accountId}/export")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDTO>> exportTransactions(@PathVariable Long accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<TransactionDTO> transactions = transactionService.exportTransactions(accountId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}
