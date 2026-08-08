package com.example.backend.service.impl;

import com.example.backend.dto.AccountDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.User;
import com.example.backend.entity.enums.AccountStatus;
import com.example.backend.entity.enums.Role;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorisedAccessException;
import com.example.backend.mapper.AccountMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    // Roles allowed to view ANY account, not just their own (per SRS role list)
    private static final Set<Role> READ_ALL_ROLES = Set.of(
            Role.ADMIN, Role.DEALER, Role.COMPLIANCE_OFFICER, Role.RISK_MANAGER
    );

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountDTO createAccount(String username, AccountDTO request) {
        User user = getUserOrThrow(username);

        Account account = Account.builder()
                .user(user)
                .accountNumber(generateUniqueAccountNumber())
                .accountType(request.getAccountType())
                .balance(request.getBalance())
                .cashAvailable(request.getBalance()) // initial deposit = starting cash available
                .status(AccountStatus.ACTIVE)
                .build();

        return AccountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO getAccountById(String username, Long accountId) {
        User user = getUserOrThrow(username);
        Account account = getAccountOrThrow(accountId);

        boolean isOwner = account.getUser().getId().equals(user.getId());
        boolean canReadAny = READ_ALL_ROLES.contains(user.getRole());

        if (!isOwner && !canReadAny) {
            throw new UnauthorisedAccessException("You do not have access to this account");
        }

        return AccountMapper.toDTO(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getMyAccounts(String username) {
        User user = getUserOrThrow(username);
        return accountRepository.findByUserId(user.getId())
                .stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO updateStatus(Long accountId, AccountDTO request) {
        Account account = getAccountOrThrow(accountId);
        account.setStatus(request.getStatus());
        return AccountMapper.toDTO(accountRepository.save(account));
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private Account getAccountOrThrow(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
    }

    private String generateUniqueAccountNumber() {
        String candidate;
        do {
            candidate = "ACC" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 10).toUpperCase();
        } while (accountRepository.existsByAccountNumber(candidate));
        return candidate;
    }
}