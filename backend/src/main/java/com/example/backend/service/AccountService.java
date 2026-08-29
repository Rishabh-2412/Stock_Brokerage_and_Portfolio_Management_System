package com.example.backend.service;

import com.example.backend.dto.AccountDTO;
import com.example.backend.dto.request.FundTransferRequest;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(String username, AccountDTO request);

    AccountDTO fundAccount(String username, FundTransferRequest request);

    AccountDTO getAccountById(String username, Long accountId);

    List<AccountDTO> getMyAccounts(String username);

    List<AccountDTO> getAllAccounts();

    AccountDTO updateStatus(Long accountId, AccountDTO request);
}