package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.util.List;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
 
    private Long accountId;
    private String accountNumber;
    private BigDecimal totalBalance;
    private BigDecimal cashAvailable;
    private BigDecimal totalInvested;
    private BigDecimal totalGain;
    private BigDecimal totalGainPercentage;
    private List<HoldingDTO> holdings;
 
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HoldingDTO {
        private Long holdingId;
        private Long securityId;
        private String symbol;
        private String securityName;
        private Long quantity;
        private BigDecimal averageCost;
        private BigDecimal currentPrice;
        private BigDecimal currentValue;
        private BigDecimal gainLoss;
        private BigDecimal gainLossPercentage;
    }
 
}