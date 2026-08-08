package com.example.backend.service.impl;

import com.example.backend.dto.WatchlistDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.Security;
import com.example.backend.entity.User;
import com.example.backend.entity.Watchlist;
import com.example.backend.exception.DuplicateTradeException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorisedAccessException;
import com.example.backend.mapper.WatchlistMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.WatchlistRepository;
import com.example.backend.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Client-only feature: every method verifies the calling user owns the
 * account in question. No ADMIN/DEALER override here, unlike Accounts and
 * Securities - a watchlist is a personal list, not shared data.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final AccountRepository accountRepository;
    private final SecurityRepository securityRepository;
    private final UserRepository userRepository;

    @Override
    public WatchlistDTO addToWatchlist(String username, WatchlistDTO request) {
        User user = getUserOrThrow(username);
        Account account = getOwnedAccountOrThrow(request.getAccountId(), user);
        Security security = securityRepository.findById(request.getSecurityId())
                .orElseThrow(() -> new ResourceNotFoundException("Security not found: " + request.getSecurityId()));

        if (watchlistRepository.existsByAccountIdAndSecurityId(account.getId(), security.getId())) {
            throw new DuplicateTradeException("This security is already on the watchlist for this account");
        }

        Watchlist watchlist = Watchlist.builder()
                .account(account)
                .security(security)
                .addedAt(LocalDateTime.now())
                .build();

        return WatchlistMapper.toDTO(watchlistRepository.save(watchlist));
    }

    @Override
    public void removeFromWatchlist(String username, Long watchlistId) {
        User user = getUserOrThrow(username);
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist entry not found: " + watchlistId));

        if (!watchlist.getAccount().getUser().getId().equals(user.getId())) {
            throw new UnauthorisedAccessException("You do not have access to this watchlist entry");
        }

        watchlistRepository.delete(watchlist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WatchlistDTO> getWatchlistForAccount(String username, Long accountId) {
        User user = getUserOrThrow(username);
        Account account = getOwnedAccountOrThrow(accountId, user);

        return watchlistRepository.findByAccountId(account.getId())
                .stream()
                .map(WatchlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private Account getOwnedAccountOrThrow(Long accountId, User user) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new UnauthorisedAccessException("You do not have access to this account's watchlist");
        }
        return account;
    }
}