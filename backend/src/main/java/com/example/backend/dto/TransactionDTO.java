package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
 
    private Long transactionId;
    private Long accountId;
    private Long securityId;
    private String transactionType;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private BigDecimal commission;
    private String status;
    private LocalDateTime transactionDate;
    private LocalDateTime settlementDate;
 
}