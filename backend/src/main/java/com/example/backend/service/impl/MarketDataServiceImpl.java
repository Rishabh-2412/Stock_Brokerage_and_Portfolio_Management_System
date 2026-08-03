package com.example.backend.service.impl;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.entity.PriceHistory;
import com.example.backend.entity.Security;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.SecurityMapper;
import com.example.backend.repository.PriceHistoryRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private SecurityMapper securityMapper;

    @Override
    public Double getCurrentPrice(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));
        return security.getCurrentPrice();
    }

    @Override
    public Double getCurrentPriceBySymbol(String symbol) {
        Security security = securityRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with symbol: " + symbol));
        return security.getCurrentPrice();
    }

    @Override
    public SecurityDTO getSecurityDetails(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));
        return securityMapper.toDTO(security);
    }

    @Override
    public List<PriceHistory> getPriceHistory(Long securityId, LocalDate startDate, LocalDate endDate) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        return priceHistoryRepository.findBySecurityAndDateBetween(security, startDate, endDate);
    }

    @Override
    public List<PriceHistory> getPriceHistory(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        return priceHistoryRepository.findBySecurity(security);
    }

    @Override
    public PriceHistory getLatestPriceHistory(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        List<PriceHistory> history = priceHistoryRepository.findBySecurity(security);
        return history.isEmpty() ? null : history.get(history.size() - 1);
    }

    @Override
    public void recordPriceHistory(Long securityId, Double openPrice, Double highPrice, 
                                   Double lowPrice, Double closePrice, Long volume) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        PriceHistory history = new PriceHistory();
        history.setSecurity(security);
        history.setOpenPrice(openPrice);
        history.setHighPrice(highPrice);
        history.setLowPrice(lowPrice);
        history.setClosePrice(closePrice);
        history.setVolume(volume);
        history.setDate(LocalDate.now());

        priceHistoryRepository.save(history);
        
        security.setCurrentPrice(closePrice);
        securityRepository.save(security);
    }

    @Override
    public List<SecurityDTO> getTopGainers() {
        return securityRepository.findAll().stream()
                .sorted((a, b) -> Double.compare(b.getCurrentPrice(), a.getCurrentPrice()))
                .limit(10)
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SecurityDTO> getTopLosers() {
        return securityRepository.findAll().stream()
                .sorted((a, b) -> Double.compare(a.getCurrentPrice(), b.getCurrentPrice()))
                .limit(10)
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SecurityDTO> getMostActive() {
        // Logic to get most active securities based on volume
        return securityRepository.findAll().stream()
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Double getPriceChange(Long securityId, LocalDate startDate, LocalDate endDate) {
        List<PriceHistory> history = getPriceHistory(securityId, startDate, endDate);
        if (history.isEmpty()) {
            return 0.0;
        }
        Double openPrice = history.get(0).getOpenPrice();
        Double closePrice = history.get(history.size() - 1).getClosePrice();
        return closePrice - openPrice;
    }

    @Override
    public Double getPriceChangePercentage(Long securityId, LocalDate startDate, LocalDate endDate) {
        List<PriceHistory> history = getPriceHistory(securityId, startDate, endDate);
        if (history.isEmpty() || history.get(0).getOpenPrice() == 0) {
            return 0.0;
        }
        Double openPrice = history.get(0).getOpenPrice();
        Double closePrice = history.get(history.size() - 1).getClosePrice();
        return ((closePrice - openPrice) / openPrice) * 100;
    }

    @Override
    public Double getMarketCap(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));
        return security.getMarketCap();
    }
}