package com.example.backend.controller;

import com.example.backend.dto.PriceHistoryDTO;
import com.example.backend.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Optional stretch module: daily OHLCV history for charting. ADMIN adds
 * records manually (no real exchange feed in this mini project); any
 * logged-in user can read.
 */
@RestController
@RequestMapping("/api/market/price-history")
@RequiredArgsConstructor
@Tag(name = "Market Data", description = "Daily OHLCV price history for charting - optional stretch module")
@SecurityRequirement(name = "bearerAuth")
public class MarketController {

    private final MarketDataService marketDataService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a daily OHLCV record for a security (ADMIN only)")
    public ResponseEntity<PriceHistoryDTO> addPriceRecord(@Valid @RequestBody PriceHistoryDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketDataService.addPriceRecord(request));
    }

    @GetMapping("/{securityId}")
    @Operation(summary = "Get price history for a security, optionally in a date range",
            description = "Provide both startDate and endDate to filter, or omit both for the full history")
    public ResponseEntity<List<PriceHistoryDTO>> getHistory(
            @PathVariable Long securityId,
            @Parameter(description = "Inclusive start date, e.g. 2026-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Inclusive end date, e.g. 2026-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(marketDataService.getHistory(securityId, startDate, endDate));
    }
}