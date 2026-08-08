package com.example.backend.service.impl;

import com.example.backend.dto.response.PortfolioResponse;
import com.example.backend.entity.Account;
import com.example.backend.entity.User;
import com.example.backend.entity.enums.Role;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorisedAccessException;
import com.example.backend.mapper.PortfolioMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.HoldingsRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Read-only. Every call recomputes everything fresh from Holdings +
 * Security.currentPrice - nothing here is cached or stored, so the numbers
 * are always live as of the moment you call this endpoint.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioServiceImpl implements PortfolioService {

    private static final Set<Role> READ_ALL_ROLES = Set.of(
            Role.ADMIN, Role.DEALER, Role.COMPLIANCE_OFFICER, Role.RISK_MANAGER
    );

    private final AccountRepository accountRepository;
    private final HoldingsRepository holdingsRepository;
    private final UserRepository userRepository;

    @Override
    public PortfolioResponse getPortfolio(String username, Long accountId) {
        User user = getUserOrThrow(username);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        boolean isOwner = account.getUser().getId().equals(user.getId());
        boolean canReadAny = READ_ALL_ROLES.contains(user.getRole());

        if (!isOwner && !canReadAny) {
            throw new UnauthorisedAccessException("You do not have access to this account's portfolio");
        }

        return PortfolioMapper.toResponse(accountId, holdingsRepository.findByAccountId(accountId));
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}