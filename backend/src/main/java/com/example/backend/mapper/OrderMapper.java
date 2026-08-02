package com.example.backend.mapper;

import com.example.backend.dto.OrderDTO;
import com.example.backend.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setAccountId(order.getAccountId());
        orderDTO.setSecurityId(order.getSecurityId());
        orderDTO.setOrderType(order.getOrderType());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setFilledQuantity(order.getFilledQuantity());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());

        return orderDTO;
    }

    public Order toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setAccountId(orderDTO.getAccountId());
        order.setSecurityId(orderDTO.getSecurityId());
        order.setOrderType(orderDTO.getOrderType());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setQuantity(orderDTO.getQuantity());
        order.setPrice(orderDTO.getPrice());
        order.setFilledQuantity(orderDTO.getFilledQuantity());

        return order;
    }
}