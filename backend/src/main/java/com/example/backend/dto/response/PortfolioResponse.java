package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.util.List;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {
 
    private Long accountId;
    private String accountNumber;
    private BigDecimal totalBalance;
    private BigDecimal cashAvailable;
    private BigDecimal totalInvested;
    private BigDecimal totalGain;
    private BigDecimal totalGainPercentage;
    private List<HoldingDetail> holdings;
 
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HoldingDetail {
        private Long holdingId;
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
