package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
 
    private Long accountId;
    private Long userId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private BigDecimal cashAvailable;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
 
}