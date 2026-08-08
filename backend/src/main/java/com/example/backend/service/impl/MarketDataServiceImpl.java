package com.example.backend.service.impl;

import com.example.backend.dto.PriceHistoryDTO;
import com.example.backend.entity.PriceHistory;
import com.example.backend.entity.Security;
import com.example.backend.exception.DuplicateTradeException;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.PriceHistoryMapper;
import com.example.backend.repository.PriceHistoryRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketDataServiceImpl implements MarketDataService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final SecurityRepository securityRepository;

    @Override
    public PriceHistoryDTO addPriceRecord(PriceHistoryDTO request) {
        Security security = securityRepository.findById(request.getSecurityId())
                .orElseThrow(() -> new ResourceNotFoundException("Security not found: " + request.getSecurityId()));

        if (priceHistoryRepository.findBySecurityIdAndDate(security.getId(), request.getDate()).isPresent()) {
            throw new DuplicateTradeException(
                    "A price history record already exists for " + security.getSymbol() + " on " + request.getDate());
        }

        if (request.getHighPrice().compareTo(request.getLowPrice()) < 0) {
            throw new InvalidOrderException("highPrice cannot be less than lowPrice");
        }

        PriceHistory history = PriceHistory.builder()
                .security(security)
                .openPrice(request.getOpenPrice())
                .highPrice(request.getHighPrice())
                .lowPrice(request.getLowPrice())
                .closePrice(request.getClosePrice())
                .volume(request.getVolume())
                .date(request.getDate())
                .build();

        return PriceHistoryMapper.toDTO(priceHistoryRepository.save(history));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceHistoryDTO> getHistory(Long securityId, LocalDate startDate, LocalDate endDate) {
        if (!securityRepository.existsById(securityId)) {
            throw new ResourceNotFoundException("Security not found: " + securityId);
        }

        List<PriceHistory> records = (startDate != null && endDate != null)
                ? priceHistoryRepository.findBySecurityIdAndDateBetweenOrderByDateAsc(securityId, startDate, endDate)
                : priceHistoryRepository.findBySecurityIdOrderByDateAsc(securityId);

        return records.stream()
                .map(PriceHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}