package com.example.backend.controller;

import com.example.backend.dto.AccountDTO;
import com.example.backend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Trading account management")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Open a new trading account for the logged-in user",
            description = "Only accountType and balance (used as initial deposit) are read from the body")
    public ResponseEntity<AccountDTO> createAccount(
            Authentication authentication,
            @Valid @RequestBody AccountDTO request) {
        AccountDTO response = accountService.createAccount(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Operation(summary = "List all accounts belonging to the logged-in user")
    public ResponseEntity<List<AccountDTO>> getMyAccounts(Authentication authentication) {
        return ResponseEntity.ok(accountService.getMyAccounts(authentication.getName()));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get one account by id (owner, or ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER)")
    public ResponseEntity<AccountDTO> getAccount(
            Authentication authentication,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(authentication.getName(), accountId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEALER','COMPLIANCE_OFFICER','RISK_MANAGER')")
    @Operation(summary = "List all accounts (ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER only)")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/{accountId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an account's status (ADMIN only)",
            description = "Only the status field is read from the body")
    public ResponseEntity<AccountDTO> updateStatus(
            @PathVariable Long accountId,
            @Valid @RequestBody AccountDTO request) {
        return ResponseEntity.ok(accountService.updateStatus(accountId, request));
    }
}