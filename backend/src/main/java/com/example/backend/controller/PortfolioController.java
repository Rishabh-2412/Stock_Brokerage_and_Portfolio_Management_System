package com.example.backend.controller;

import com.example.backend.dto.PortfolioDTO;
import com.example.backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
 
@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {
 
    @Autowired
    private PortfolioService portfolioService;
 
    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PortfolioDTO> getPortfolioByAccountId(@PathVariable Long accountId) {
        PortfolioDTO portfolio = portfolioService.getPortfolioByAccountId(accountId);
        return ResponseEntity.ok(portfolio);
    }
 
    @GetMapping("/account/{accountId}/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getPortfolioSummary(@PathVariable Long accountId) {
        Map<String, Object> summary = portfolioService.getPortfolioSummary(accountId);
        return ResponseEntity.ok(summary);
    }
 
    @GetMapping("/account/{accountId}/total-value")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Double> getTotalPortfolioValue(@PathVariable Long accountId) {
        Double totalValue = portfolioService.getTotalPortfolioValue(accountId);
        return ResponseEntity.ok(totalValue);
    }
 
    @GetMapping("/account/{accountId}/total-invested")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Double> getTotalInvestedAmount(@PathVariable Long accountId) {
        Double totalInvested = portfolioService.getTotalInvestedAmount(accountId);
        return ResponseEntity.ok(totalInvested);
    }
 
    @GetMapping("/account/{accountId}/total-gain-loss")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Double> getTotalGainLoss(@PathVariable Long accountId) {
        Double gainLoss = portfolioService.getTotalGainLoss(accountId);
        return ResponseEntity.ok(gainLoss);
    }
 
    @GetMapping("/account/{accountId}/gain-loss-percentage")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Double> getGainLossPercentage(@PathVariable Long accountId) {
        Double percentage = portfolioService.getGainLossPercentage(accountId);
        return ResponseEntity.ok(percentage);
    }
 
    @GetMapping("/account/{accountId}/holdings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PortfolioDTO>> getHoldingsByAccountId(@PathVariable Long accountId) {
        List<PortfolioDTO> holdings = portfolioService.getHoldingsByAccountId(accountId);
        return ResponseEntity.ok(holdings);
    }
 
    @GetMapping("/account/{accountId}/allocation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Double>> getPortfolioAllocation(@PathVariable Long accountId) {
        Map<String, Double> allocation = portfolioService.getPortfolioAllocation(accountId);
        return ResponseEntity.ok(allocation);
    }
 
    @GetMapping("/account/{accountId}/sector-allocation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Double>> getSectorAllocation(@PathVariable Long accountId) {
        Map<String, Double> sectorAllocation = portfolioService.getSectorAllocation(accountId);
        return ResponseEntity.ok(sectorAllocation);
    }
 
    @GetMapping("/account/{accountId}/top-holdings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PortfolioDTO>> getTopHoldings(@PathVariable Long accountId, @RequestParam(defaultValue = "5") int limit) {
        List<PortfolioDTO> topHoldings = portfolioService.getTopHoldings(accountId, limit);
        return ResponseEntity.ok(topHoldings);
    }
 
    @GetMapping("/account/{accountId}/performance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getPortfolioPerformance(@PathVariable Long accountId) {
        Map<String, Object> performance = portfolioService.getPortfolioPerformance(accountId);
        return ResponseEntity.ok(performance);
    }
 
    @GetMapping("/account/{accountId}/diversification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Double>> getDiversificationAnalysis(@PathVariable Long accountId) {
        Map<String, Double> diversification = portfolioService.getDiversificationAnalysis(accountId);
        return ResponseEntity.ok(diversification);
    }
}