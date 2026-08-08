package com.example.backend.service;

import com.example.backend.dto.request.PlaceOrderRequest;
import com.example.backend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(String username, PlaceOrderRequest request);

    OrderResponse cancelOrder(String username, Long orderId);

    OrderResponse getOrderById(String username, Long orderId);

    List<OrderResponse> getOrdersForAccount(String username, Long accountId);

    List<OrderResponse> getAllOrders();
}