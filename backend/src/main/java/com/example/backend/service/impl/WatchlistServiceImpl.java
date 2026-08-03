package com.example.backend.service.impl;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.Security;
import com.example.backend.entity.Watchlist;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.SecurityMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.repository.WatchlistRepository;
import com.example.backend.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private SecurityMapper securityMapper;

    @Override
    public void addToWatchlist(Long accountId, Long securityId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        boolean exists = watchlistRepository.existsByAccountAndSecurity(account, security);
        if (exists) {
            throw new IllegalArgumentException("Security already in watchlist");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setAccount(account);
        watchlist.setSecurity(security);
        watchlist.setAddedAt(LocalDateTime.now());

        watchlistRepository.save(watchlist);
    }

    @Override
    public void removeFromWatchlist(Long accountId, Long securityId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        Watchlist watchlist = watchlistRepository.findByAccountAndSecurity(account, security)
                .orElseThrow(() -> new ResourceNotFoundException("Security not in watchlist"));

        watchlistRepository.delete(watchlist);
    }

    @Override
    public List<SecurityDTO> getWatchlist(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return watchlistRepository.findByAccount(account).stream()
                .map(Watchlist::getSecurity)
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isInWatchlist(Long accountId, Long securityId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        return watchlistRepository.existsByAccountAndSecurity(account, security);
    }

    @Override
    public long getWatchlistSize(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return watchlistRepository.countByAccount(account);
    }

    @Override
    public void clearWatchlist(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Watchlist> watchlistItems = watchlistRepository.findByAccount(account);
        watchlistRepository.deleteAll(watchlistItems);
    }

    @Override
    public void addToWatchlistBySymbol(Long accountId, String symbol) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with symbol: " + symbol));

        addToWatchlist(accountId, security.getSecurityId());
    }

    @Override
    public void removeFromWatchlistBySymbol(Long accountId, String symbol) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with symbol: " + symbol));

        removeFromWatchlist(accountId, security.getSecurityId());
    }
}