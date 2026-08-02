package com.example.backend.controller;

import com.example.backend.dto.OrderDTO;
import com.example.backend.dto.request.PlaceOrderRequest;
import com.example.backend.dto.response.OrderResponse;
import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import javax.validation.Valid;
import java.util.List;
 
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
 
    @Autowired
    private OrderService orderService;
 
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        OrderResponse response = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
 
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
 
    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOrdersByAccountId(@PathVariable Long accountId) {
        List<OrderDTO> orders = orderService.getOrdersByAccountId(accountId);
        return ResponseEntity.ok(orders);
    }
 
    @GetMapping("/account/{accountId}/pending")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getPendingOrdersByAccountId(@PathVariable Long accountId) {
        List<OrderDTO> orders = orderService.getPendingOrdersByAccountId(accountId);
        return ResponseEntity.ok(orders);
    }
 
    @GetMapping("/security/{securityId}/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOrdersBySecurity(@PathVariable Long securityId, @PathVariable Long accountId) {
        List<OrderDTO> orders = orderService.getOrdersBySecurityAndAccount(securityId, accountId);
        return ResponseEntity.ok(orders);
    }
 
    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        OrderDTO order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(order);
    }
 
    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(orderId, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }
 
    @GetMapping("/account/{accountId}/open")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOpenOrdersByAccountId(@PathVariable Long accountId) {
        List<OrderDTO> orders = orderService.getOpenOrdersByAccountId(accountId);
        return ResponseEntity.ok(orders);
    }
 
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }
 
    @GetMapping("/account/{accountId}/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOrderHistoryByAccountId(@PathVariable Long accountId) {
        List<OrderDTO> orders = orderService.getOrderHistoryByAccountId(accountId);
        return ResponseEntity.ok(orders);
    }
}
