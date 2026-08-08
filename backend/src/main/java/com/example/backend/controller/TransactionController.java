package com.example.backend.controller;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.entity.enums.TransactionType;
import com.example.backend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * UPDATED: GET /api/transactions/account/{accountId} now accepts optional
 * type/startDate/endDate query params for filtering (Module 8: Transaction
 * History). All three are optional - omit any/all to get the unfiltered list.
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Order execution and trade history")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/execute/{orderId}")
    @PreAuthorize("hasAnyRole('CLIENT','DEALER','ADMIN')")
    @Operation(summary = "Execute a PENDING order",
            description = "Fully fills the order at its stored price: creates a Transaction, updates Holdings, and moves cash on the Account. No request body needed.")
    public ResponseEntity<TransactionDTO> executeOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        TransactionDTO response = transactionService.executeOrder(authentication.getName(), orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get one transaction by id (owner, or ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER)")
    public ResponseEntity<TransactionDTO> getTransaction(
            Authentication authentication,
            @PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(authentication.getName(), transactionId));
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "List transactions for an account, optionally filtered (owner, or ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER)",
            description = "All filters are optional. Omit them all for the full history.")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForAccount(
            Authentication authentication,
            @PathVariable Long accountId,
            @Parameter(description = "Filter by BUY or SELL (DIVIDEND also valid if you use it elsewhere)")
            @RequestParam(required = false) TransactionType type,
            @Parameter(description = "Inclusive start date, e.g. 2026-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Inclusive end date, e.g. 2026-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.getTransactionsForAccount(
                authentication.getName(), accountId, type, startDate, endDate));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEALER','COMPLIANCE_OFFICER','RISK_MANAGER')")
    @Operation(summary = "List all transactions across all accounts (ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER only)")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}