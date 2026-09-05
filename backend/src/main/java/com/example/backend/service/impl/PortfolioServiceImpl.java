package com.example.backend.service.impl;

import com.example.backend.dto.response.PortfolioResponse;
import com.example.backend.entity.Account;
import com.example.backend.entity.Holdings;
import com.example.backend.entity.PriceHistory;
import com.example.backend.entity.User;
import com.example.backend.entity.enums.Role;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorisedAccessException;
import com.example.backend.mapper.PortfolioMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.HoldingsRepository;
import com.example.backend.repository.PriceHistoryRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final PriceHistoryRepository priceHistoryRepository;

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

        List<Holdings> holdings = holdingsRepository.findByAccountId(accountId);
        Map<Long, BigDecimal> previousCloseBySecurityId = buildPreviousCloseLookup(holdings);

        return PortfolioMapper.toResponse(accountId, holdings, previousCloseBySecurityId);
    }

    /**
     * One lookup per DISTINCT security in this account's holdings (not per
     * holding row) - a client rarely holds the same security twice, but this
     * keeps it correct and avoids redundant queries either way.
     *
     * For each security: find the most recent price_history close strictly
     * before today. If none exists yet (brand new security with no history,
     * or history was only ever entered for today), we fall back to the
     * security's own currentPrice - which makes todaysPL come out to exactly
     * 0 for that holding, an honest "we don't know today's move yet" rather
     * than a fabricated number.
     */
    private Map<Long, BigDecimal> buildPreviousCloseLookup(List<Holdings> holdings) {
        Map<Long, BigDecimal> result = new HashMap<>();
        LocalDate today = LocalDate.now();

        for (Holdings holding : holdings) {
            Long securityId = holding.getSecurity().getId();
            if (result.containsKey(securityId)) {
                continue;
            }

            BigDecimal previousClose = priceHistoryRepository
                    .findFirstBySecurityIdAndDateLessThanOrderByDateDesc(securityId, today)
                    .map(PriceHistory::getClosePrice)
                    .orElse(holding.getSecurity().getCurrentPrice());

            result.put(securityId, previousClose);
        }

        return result;
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}