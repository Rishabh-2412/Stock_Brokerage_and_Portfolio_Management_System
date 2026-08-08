package com.example.backend.mapper;

import com.example.backend.dto.AccountDTO;
import com.example.backend.entity.Account;

public class AccountMapper {

    private AccountMapper() {
    }

    public static AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }
        return AccountDTO.builder()
                .accountId(account.getId())
                .userId(account.getUser() != null ? account.getUser().getId() : null)
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .cashAvailable(account.getCashAvailable())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}