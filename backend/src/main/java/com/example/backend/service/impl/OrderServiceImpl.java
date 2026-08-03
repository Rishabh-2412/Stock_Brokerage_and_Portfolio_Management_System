package com.example.backend.service.impl;

import com.example.backend.dto.OrderDTO;
import com.example.backend.dto.request.PlaceOrderRequest;
import com.example.backend.entity.Account;
import com.example.backend.entity.Order;
import com.example.backend.entity.Security;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.InsufficientMarginException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.service.OrderService;
import com.example.backend.service.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderValidator orderValidator;

    @Override
    public OrderDTO placeOrder(Long accountId, PlaceOrderRequest orderRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Security security = securityRepository.findBySymbol(orderRequest.getSymbol())
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with symbol: " + orderRequest.getSymbol()));

        orderValidator.validateOrder(orderRequest, account, security);

        Order order = new Order();
        order.setAccount(account);
        order.setSecurity(security);
        order.setOrderType(orderRequest.getOrderType());
        order.setOrderStatus("pending");
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(orderRequest.getPrice());
        order.setFilledQuantity(0L);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return orderRepository.findByAccount(account).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(Long accountId, String status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return orderRepository.findByAccountAndOrderStatus(account, status).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if ("completed".equalsIgnoreCase(order.getOrderStatus())) {
            throw new InvalidOrderException("Cannot cancel a completed order");
        }

        order.setOrderStatus("cancelled");
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public OrderDTO fillOrder(Long orderId, Long filledQuantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (filledQuantity > order.getQuantity()) {
            throw new InvalidOrderException("Filled quantity cannot exceed order quantity");
        }

        order.setFilledQuantity(filledQuantity);
        if (filledQuantity.equals(order.getQuantity())) {
            order.setOrderStatus("completed");
        } else {
            order.setOrderStatus("partially_filled");
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public List<OrderDTO> getPendingOrders(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return orderRepository.findByAccountAndOrderStatus(account, "pending").stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        orderRepository.delete(order);
    }
}