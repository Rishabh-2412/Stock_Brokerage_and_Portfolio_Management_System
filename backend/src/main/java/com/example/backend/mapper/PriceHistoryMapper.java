package com.example.backend.mapper;

import com.example.backend.dto.PriceHistoryDTO;
import com.example.backend.entity.PriceHistory;

public class PriceHistoryMapper {

    private PriceHistoryMapper() {
    }

    public static PriceHistoryDTO toDTO(PriceHistory history) {
        if (history == null) {
            return null;
        }
        return PriceHistoryDTO.builder()
                .historyId(history.getId())
                .securityId(history.getSecurity() != null ? history.getSecurity().getId() : null)
                .symbol(history.getSecurity() != null ? history.getSecurity().getSymbol() : null)
                .openPrice(history.getOpenPrice())
                .highPrice(history.getHighPrice())
                .lowPrice(history.getLowPrice())
                .closePrice(history.getClosePrice())
                .volume(history.getVolume())
                .date(history.getDate())
                .build();
    }
}