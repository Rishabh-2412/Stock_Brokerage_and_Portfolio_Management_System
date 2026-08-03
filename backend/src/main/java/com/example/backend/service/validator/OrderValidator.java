package com.example.backend.service.validator;

import com.example.backend.entity.Account;
import com.example.backend.entity.Order;
import com.example.backend.entity.Security;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.InsufficientMarginException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validateOrderCreation(Order order, Account account, Security security) {
        validateOrderQuantity(order.getQuantity());
        validateOrderPrice(order.getPrice());
        validateAccountStatus(account);
        validateSecurityExists(security);
        validateSufficientFunds(order, account, security);
    }

    public void validateOrderQuantity(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOrderException("Order quantity must be greater than 0");
        }
    }

    public void validateOrderPrice(Double price) {
        if (price == null || price <= 0) {
            throw new InvalidOrderException("Order price must be greater than 0");
        }
    }

    public void validateAccountStatus(Account account) {
        if (account == null || !account.getStatus().equals("active")) {
            throw new InvalidOrderException("Account is not active");
        }
    }

    public void validateSecurityExists(Security security) {
        if (security == null) {
            throw new InvalidOrderException("Security does not exist");
        }
    }

    public void validateSufficientFunds(Order order, Account account, Security security) {
        Double requiredAmount = order.getQuantity() * order.getPrice();
        if (order.getOrderType().equals("buy")) {
            if (account.getCashAvailable() < requiredAmount) {
                throw new InsufficientMarginException("Insufficient funds for this order");
            }
        }
    }

    public void validateOrderUpdate(Order order, String newStatus) {
        if (newStatus == null || newStatus.isEmpty()) {
            throw new InvalidOrderException("Order status cannot be empty");
        }
        validateOrderStatus(newStatus);
    }

    public void validateOrderStatus(String status) {
        if (!status.equals("pending") && !status.equals("market") && 
            !status.equals("limit") && !status.equals("stop")) {
            throw new InvalidOrderException("Invalid order status");
        }
    }
}