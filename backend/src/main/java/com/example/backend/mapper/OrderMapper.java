package com.example.backend.mapper;

import com.example.backend.dto.response.OrderResponse;
import com.example.backend.entity.Order;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        return OrderResponse.builder()
                .orderId(order.getId())
                .accountId(order.getAccount() != null ? order.getAccount().getId() : null)
                .securityId(order.getSecurity() != null ? order.getSecurity().getId() : null)
                .symbol(order.getSecurity() != null ? order.getSecurity().getSymbol() : null)
                .orderType(order.getOrderType())
                .priceType(order.getPriceType())
                .orderStatus(order.getOrderStatus())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .filledQuantity(order.getFilledQuantity())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}