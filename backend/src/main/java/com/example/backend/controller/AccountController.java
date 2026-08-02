package com.example.backend.controller;

import com.example.backend.dto.AccountDTO;
import com.example.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import javax.validation.Valid;
import java.util.List;
 
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
 
    @Autowired
    private AccountService accountService;
 
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
 
    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long accountId) {
        AccountDTO account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }
 
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable Long userId) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }
 
    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }
 
    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> closeAccount(@PathVariable Long accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.ok("Account closed successfully");
    }
 
    @GetMapping("/{accountId}/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Double> getAccountBalance(@PathVariable Long accountId) {
        Double balance = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }
 
    @GetMapping("/{accountId}/cash-available")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Double> getCashAvailable(@PathVariable Long accountId) {
        Double cashAvailable = accountService.getCashAvailable(accountId);
        return ResponseEntity.ok(cashAvailable);
    }
 
    @PutMapping("/{accountId}/deposit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountDTO> depositFunds(@PathVariable Long accountId, @RequestParam Double amount) {
        AccountDTO account = accountService.depositFunds(accountId, amount);
        return ResponseEntity.ok(account);
    }
 
    @PutMapping("/{accountId}/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountDTO> withdrawFunds(@PathVariable Long accountId, @RequestParam Double amount) {
        AccountDTO account = accountService.withdrawFunds(accountId, amount);
        return ResponseEntity.ok(account);
    }
 
    @GetMapping("/{accountId}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getAccountStatus(@PathVariable Long accountId) {
        String status = accountService.getAccountStatus(accountId);
        return ResponseEntity.ok(status);
    }
 
    @PutMapping("/{accountId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable Long accountId, @RequestParam String status) {
        AccountDTO account = accountService.updateAccountStatus(accountId, status);
        return ResponseEntity.ok(account);
    }
}