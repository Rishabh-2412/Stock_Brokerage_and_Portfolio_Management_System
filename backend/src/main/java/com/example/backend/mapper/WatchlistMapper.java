package com.example.backend.mapper;

import com.example.backend.dto.WatchlistDTO;
import com.example.backend.entity.Watchlist;

public class WatchlistMapper {

    private WatchlistMapper() {
    }

    public static WatchlistDTO toDTO(Watchlist watchlist) {
        if (watchlist == null) {
            return null;
        }
        return WatchlistDTO.builder()
                .watchlistId(watchlist.getId())
                .accountId(watchlist.getAccount() != null ? watchlist.getAccount().getId() : null)
                .securityId(watchlist.getSecurity() != null ? watchlist.getSecurity().getId() : null)
                .symbol(watchlist.getSecurity() != null ? watchlist.getSecurity().getSymbol() : null)
                .securityName(watchlist.getSecurity() != null ? watchlist.getSecurity().getName() : null)
                .currentPrice(watchlist.getSecurity() != null ? watchlist.getSecurity().getCurrentPrice() : null)
                .addedAt(watchlist.getAddedAt())
                .build();
    }
}