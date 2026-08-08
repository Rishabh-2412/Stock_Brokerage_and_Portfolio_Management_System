package com.example.backend.service;

import com.example.backend.dto.PriceHistoryDTO;

import java.time.LocalDate;
import java.util.List;

public interface MarketDataService {

    PriceHistoryDTO addPriceRecord(PriceHistoryDTO request);

    List<PriceHistoryDTO> getHistory(Long securityId, LocalDate startDate, LocalDate endDate);
}