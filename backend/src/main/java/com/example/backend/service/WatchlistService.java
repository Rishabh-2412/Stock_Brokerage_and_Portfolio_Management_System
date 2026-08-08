package com.example.backend.service;

import com.example.backend.dto.WatchlistDTO;

import java.util.List;

public interface WatchlistService {

    WatchlistDTO addToWatchlist(String username, WatchlistDTO request);

    void removeFromWatchlist(String username, Long watchlistId);

    List<WatchlistDTO> getWatchlistForAccount(String username, Long accountId);
}