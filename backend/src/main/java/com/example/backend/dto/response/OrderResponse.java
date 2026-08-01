package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
 
    private Long orderId;
    private Long accountId;
    private String symbol;
    private String orderType;
    private String orderStatus;
    private Long quantity;
    private BigDecimal price;
    private Long filledQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
 
}