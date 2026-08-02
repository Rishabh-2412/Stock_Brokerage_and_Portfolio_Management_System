package com.example.backend.controller;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/watchlist")
@CrossOrigin(origins = "*")
public class WatchlistController {
 
    @Autowired
    private WatchlistService watchlistService;
 
    @PostMapping("/account/{accountId}/security/{securityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addToWatchlist(@PathVariable Long accountId, @PathVariable Long securityId) {
        watchlistService.addToWatchlist(accountId, securityId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Security added to watchlist");
    }
 
    @DeleteMapping("/account/{accountId}/security/{securityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable Long accountId, @PathVariable Long securityId) {
        watchlistService.removeFromWatchlist(accountId, securityId);
        return ResponseEntity.ok("Security removed from watchlist");
    }
 
    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SecurityDTO>> getWatchlistByAccountId(@PathVariable Long accountId) {
        List<SecurityDTO> watchlist = watchlistService.getWatchlistByAccountId(accountId);
        return ResponseEntity.ok(watchlist);
    }
 
    @GetMapping("/account/{accountId}/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> getWatchlistCount(@PathVariable Long accountId) {
        Integer count = watchlistService.getWatchlistCount(accountId);
        return ResponseEntity.ok(count);
    }
 
    @GetMapping("/account/{accountId}/contains/{securityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> isSecurityInWatchlist(@PathVariable Long accountId, @PathVariable Long securityId) {
        Boolean isInWatchlist = watchlistService.isSecurityInWatchlist(accountId, securityId);
        return ResponseEntity.ok(isInWatchlist);
    }
 
    @GetMapping("/account/{accountId}/by-symbol/{symbol}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SecurityDTO> getWatchlistItemBySymbol(@PathVariable Long accountId, @PathVariable String symbol) {
        SecurityDTO security = watchlistService.getWatchlistItemBySymbol(accountId, symbol);
        return ResponseEntity.ok(security);
    }
 
    @PostMapping("/account/{accountId}/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> clearWatchlist(@PathVariable Long accountId) {
        watchlistService.clearWatchlist(accountId);
        return ResponseEntity.ok("Watchlist cleared successfully");
    }
 
    @GetMapping("/account/{accountId}/gainers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SecurityDTO>> getWatchlistGainers(@PathVariable Long accountId) {
        List<SecurityDTO> gainers = watchlistService.getWatchlistGainers(accountId);
        return ResponseEntity.ok(gainers);
    }
 
    @GetMapping("/account/{accountId}/losers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SecurityDTO>> getWatchlistLosers(@PathVariable Long accountId) {
        List<SecurityDTO> losers = watchlistService.getWatchlistLosers(accountId);
        return ResponseEntity.ok(losers);
    }
 
    @GetMapping("/account/{accountId}/sorted-by-price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SecurityDTO>> getWatchlistSortedByPrice(@PathVariable Long accountId) {
        List<SecurityDTO> watchlist = watchlistService.getWatchlistSortedByPrice(accountId);
        return ResponseEntity.ok(watchlist);
    }
}