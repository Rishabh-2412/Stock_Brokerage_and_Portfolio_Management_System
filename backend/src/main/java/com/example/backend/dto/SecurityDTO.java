package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityDTO {
 
    private Long securityId;
    private String symbol;
    private String name;
    private String exchange;
    private String sector;
    private BigDecimal currentPrice;
    private BigDecimal marketCap;
    private LocalDateTime lastUpdated;
 
}
