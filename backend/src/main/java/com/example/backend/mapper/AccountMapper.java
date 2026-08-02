package com.example.backend.mapper;

import com.example.backend.dto.AccountDTO;
import com.example.backend.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(account.getAccountId());
        accountDTO.setUserId(account.getUserId());
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setCashAvailable(account.getCashAvailable());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setCreatedAt(account.getCreatedAt());
        accountDTO.setUpdatedAt(account.getUpdatedAt());

        return accountDTO;
    }

    public Account toEntity(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }

        Account account = new Account();
        account.setAccountId(accountDTO.getAccountId());
        account.setUserId(accountDTO.getUserId());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setCashAvailable(accountDTO.getCashAvailable());
        account.setStatus(accountDTO.getStatus());

        return account;
    }
}