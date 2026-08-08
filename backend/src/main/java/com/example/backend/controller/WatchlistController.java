package com.example.backend.controller;

import com.example.backend.dto.WatchlistDTO;
import com.example.backend.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Tag(name = "Watchlist", description = "Per-account watchlist of securities - owner access only")
@SecurityRequirement(name = "bearerAuth")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping
    @Operation(summary = "Add a security to an account's watchlist",
            description = "The account must belong to the logged-in user")
    public ResponseEntity<WatchlistDTO> addToWatchlist(
            Authentication authentication,
            @Valid @RequestBody WatchlistDTO request) {
        WatchlistDTO response = watchlistService.addToWatchlist(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{watchlistId}")
    @Operation(summary = "Remove an entry from the watchlist")
    public ResponseEntity<Void> removeFromWatchlist(
            Authentication authentication,
            @PathVariable Long watchlistId) {
        watchlistService.removeFromWatchlist(authentication.getName(), watchlistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "List all watchlist entries for an account you own")
    public ResponseEntity<List<WatchlistDTO>> getWatchlist(
            Authentication authentication,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(watchlistService.getWatchlistForAccount(authentication.getName(), accountId));
    }
}