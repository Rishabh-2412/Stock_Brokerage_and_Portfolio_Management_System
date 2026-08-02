package com.example.backend.mapper;

import com.example.backend.dto.PortfolioDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.Holdings;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PortfolioMapper {

    public PortfolioDTO toDTO(Account account, List<Holdings> holdings) {
        if (account == null) {
            return null;
        }

        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setAccountId(account.getAccountId());
        portfolioDTO.setUserId(account.getUserId());
        portfolioDTO.setTotalBalance(account.getBalance());
        portfolioDTO.setCashAvailable(account.getCashAvailable());
        portfolioDTO.setStatus(account.getStatus());
        portfolioDTO.setHoldings(holdings);
        portfolioDTO.setCreatedAt(account.getCreatedAt());
        portfolioDTO.setUpdatedAt(account.getUpdatedAt());

        return portfolioDTO;
    }

    public PortfolioDTO toDTOWithValue(Account account, List<Holdings> holdings, double totalPortfolioValue) {
        if (account == null) {
            return null;
        }

        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setAccountId(account.getAccountId());
        portfolioDTO.setUserId(account.getUserId());
        portfolioDTO.setTotalBalance(account.getBalance());
        portfolioDTO.setCashAvailable(account.getCashAvailable());
        portfolioDTO.setTotalPortfolioValue(totalPortfolioValue);
        portfolioDTO.setStatus(account.getStatus());
        portfolioDTO.setHoldings(holdings);
        portfolioDTO.setCreatedAt(account.getCreatedAt());
        portfolioDTO.setUpdatedAt(account.getUpdatedAt());

        return portfolioDTO;
    }
}