package com.example.backend.controller;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "*")
public class MarketController {

    @Autowired
    private MarketDataService marketDataService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getMarketOverview() {
        Map<String, Object> overview = marketDataService.getMarketOverview();
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/indices")
    public ResponseEntity<Map<String, Object>> getMarketIndices() {
        Map<String, Object> indices = marketDataService.getMarketIndices();
        return ResponseEntity.ok(indices);
    }

    @GetMapping("/price-history/{securityId}")
    public ResponseEntity<List<Map<String, Object>>> getPriceHistory(@PathVariable Long securityId) {
        List<Map<String, Object>> priceHistory = marketDataService.getPriceHistory(securityId);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/price-history/{securityId}/date-range")
    public ResponseEntity<List<Map<String, Object>>> getPriceHistoryByDateRange(@PathVariable Long securityId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Map<String, Object>> priceHistory = marketDataService.getPriceHistoryByDateRange(securityId, startDate,
                endDate);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/price-history/{securityId}/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyPriceHistory(@PathVariable Long securityId) {
        List<Map<String, Object>> priceHistory = marketDataService.getDailyPriceHistory(securityId);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/price-history/{securityId}/weekly")
    public ResponseEntity<List<Map<String, Object>>> getWeeklyPriceHistory(@PathVariable Long securityId) {
        List<Map<String, Object>> priceHistory = marketDataService.getWeeklyPriceHistory(securityId);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/price-history/{securityId}/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyPriceHistory(@PathVariable Long securityId) {
        List<Map<String, Object>> priceHistory = marketDataService.getMonthlyPriceHistory(securityId);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/security/{securityId}/ohlc")
    public ResponseEntity<Map<String, Object>> getOHLCData(@PathVariable Long securityId) {
        Map<String, Object> ohlcData = marketDataService.getOHLCData(securityId);
        return ResponseEntity.ok(ohlcData);
    }

    @GetMapping("/security/{securityId}/ohlc-range")
    public ResponseEntity<List<Map<String, Object>>> getOHLCDataRange(@PathVariable Long securityId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Map<String, Object>> ohlcData = marketDataService.getOHLCDataRange(securityId, startDate, endDate);
        return ResponseEntity.ok(ohlcData);
    }

    @GetMapping("/security/{securityId}/volume")
    public ResponseEntity<Double> getTradingVolume(@PathVariable Long securityId) {
        Double volume = marketDataService.getTradingVolume(securityId);
        return ResponseEntity.ok(volume);
    }

    @GetMapping("/security/{securityId}/change-percent")
    public ResponseEntity<Double> getDayChangePercent(@PathVariable Long securityId) {
        Double changePercent = marketDataService.getDayChangePercent(securityId);
        return ResponseEntity.ok(changePercent);
    }

    @GetMapping("/security/{securityId}/52-week-high")
    public ResponseEntity<Double> get52WeekHigh(@PathVariable Long securityId) {
        Double high = marketDataService.get52WeekHigh(securityId);
        return ResponseEntity.ok(high);
    }

    @GetMapping("/security/{securityId}/52-week-low")
    public ResponseEntity<Double> get52WeekLow(@PathVariable Long securityId) {
        Double low = marketDataService.get52WeekLow(securityId);
        return ResponseEntity.ok(low);
    }

    @GetMapping("/security/{securityId}/moving-average/{days}")
    public ResponseEntity<Double> getMovingAverage(@PathVariable Long securityId, @PathVariable int days) {
        Double movingAverage = marketDataService.getMovingAverage(securityId, days);
        return ResponseEntity.ok(movingAverage);
    }

    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getMarketTrends() {
        Map<String, Object> trends = marketDataService.getMarketTrends();
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/top-movers")
    public ResponseEntity<Map<String, List<SecurityDTO>>> getTopMovers(@RequestParam(defaultValue = "5") int limit) {
        Map<String, List<SecurityDTO>> movers = marketDataService.getTopMovers(limit);
        return ResponseEntity.ok(movers);
    }
}