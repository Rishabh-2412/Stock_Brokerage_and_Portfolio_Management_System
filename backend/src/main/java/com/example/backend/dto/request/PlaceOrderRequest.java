package com.example.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
 
    @NotNull(message = "Account ID is required")
    private Long accountId;
 
    @NotBlank(message = "Security symbol is required")
    private String symbol;
 
    @NotBlank(message = "Order type is required")
    private String orderType; // buy or sell
 
    @NotBlank(message = "Order status is required")
    private String orderStatus; // market, limit, stop
 
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;
 
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price; // Required for limit and stop orders
 
    private String timeInForce; // DAY, GTC (Good Till Canceled), IOC (Immediate or Cancel)
 
}