package com.example.backend.service.impl;

import com.example.backend.dto.PortfolioDTO;
import com.example.backend.dto.response.PortfolioResponse;
import com.example.backend.entity.Account;
import com.example.backend.entity.Holdings;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.PortfolioMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.HoldingsRepository;
import com.example.backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HoldingsRepository holdingsRepository;

    @Autowired
    private PortfolioMapper portfolioMapper;

    @Override
    public PortfolioResponse getPortfolio(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Holdings> holdings = holdingsRepository.findByAccount(account);
        
        Double totalValue = holdings.stream()
                .mapToDouble(Holdings::getCurrentValue)
                .sum();

        Double totalCost = holdings.stream()
                .mapToDouble(h -> h.getAverageCost() * h.getQuantity())
                .sum();

        Double gainLoss = totalValue - totalCost;
        Double gainLossPercentage = totalCost > 0 ? (gainLoss / totalCost) * 100 : 0;

        PortfolioResponse response = new PortfolioResponse();
        response.setAccountId(accountId);
        response.setTotalPortfolioValue(totalValue);
        response.setCashBalance(account.getCashAvailable());
        response.setTotalInvested(totalCost);
        response.setGainLoss(gainLoss);
        response.setGainLossPercentage(gainLossPercentage);
        response.setHoldings(holdings.stream()
                .map(portfolioMapper::holdingsToDTO)
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public PortfolioDTO getPortfolioDTO(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Holdings> holdings = holdingsRepository.findByAccount(account);
        
        return portfolioMapper.toDTO(account, holdings);
    }

    @Override
    public Double getPortfolioValue(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Holdings> holdings = holdingsRepository.findByAccount(account);
        
        return holdings.stream()
                .mapToDouble(Holdings::getCurrentValue)
                .sum();
    }

    @Override
    public Double getTotalGainLoss(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Holdings> holdings = holdingsRepository.findByAccount(account);
        
        Double totalValue = holdings.stream()
                .mapToDouble(Holdings::getCurrentValue)
                .sum();

        Double totalCost = holdings.stream()
                .mapToDouble(h -> h.getAverageCost() * h.getQuantity())
                .sum();

        return totalValue - totalCost;
    }

    @Override
    public Double getGainLossPercentage(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Holdings> holdings = holdingsRepository.findByAccount(account);
        
        Double totalCost = holdings.stream()
                .mapToDouble(h -> h.getAverageCost() * h.getQuantity())
                .sum();

        if (totalCost == 0) {
            return 0.0;
        }

        Double gainLoss = getTotalGainLoss(accountId);
        return (gainLoss / totalCost) * 100;
    }

    @Override
    public List<Holdings> getHoldings(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return holdingsRepository.findByAccount(account);
    }

    @Override
    public void rebalancePortfolio(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Holdings> holdings = holdingsRepository.findByAccount(account);
        // Rebalancing logic implementation
    }
}