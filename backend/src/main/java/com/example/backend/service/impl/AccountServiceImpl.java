package com.example.backend.service.impl;

import com.example.backend.dto.AccountDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.AccountMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountDTO createAccount(Long userId, AccountDTO accountDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(0.0);
        account.setCashAvailable(0.0);
        account.setStatus("active");

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return accountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAccountsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return accountRepository.findByUser(user).stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO updateAccount(Long accountId, AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.setAccountType(accountDTO.getAccountType());
        account.setStatus(accountDTO.getStatus());

        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        accountRepository.delete(account);
    }

    @Override
    public void updateBalance(Long accountId, Double balance) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        account.setBalance(balance);
        accountRepository.save(account);
    }

    @Override
    public void updateCashAvailable(Long accountId, Double cashAvailable) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        account.setCashAvailable(cashAvailable);
        accountRepository.save(account);
    }

    @Override
    public Double getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return account.getBalance();
    }

    @Override
    public Double getAvailableCash(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return account.getCashAvailable();
    }

    @Override
    public boolean accountExists(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}