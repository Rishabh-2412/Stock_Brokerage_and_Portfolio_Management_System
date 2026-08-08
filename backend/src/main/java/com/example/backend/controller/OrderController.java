package com.example.backend.controller;

import com.example.backend.dto.request.PlaceOrderRequest;
import com.example.backend.dto.response.OrderResponse;
import com.example.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Placement/cancel endpoints are open to CLIENT, DEALER, ADMIN - ownership
 * is enforced inside the service (CLIENT restricted to own accounts,
 * DEALER/ADMIN unrestricted). Read-all is ADMIN/DEALER/COMPLIANCE_OFFICER/
 * RISK_MANAGER only.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order placement and cancellation - does not execute trades yet")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','DEALER','ADMIN')")
    @Operation(summary = "Place a new order",
            description = "CLIENT: own accounts only. DEALER/ADMIN: any account. Order is recorded as PENDING - it is not executed here.")
    public ResponseEntity<OrderResponse> placeOrder(
            Authentication authentication,
            @Valid @RequestBody PlaceOrderRequest request) {
        OrderResponse response = orderService.placeOrder(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CLIENT','DEALER','ADMIN')")
    @Operation(summary = "Cancel a PENDING order")
    public ResponseEntity<OrderResponse> cancelOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(authentication.getName(), orderId));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get one order by id (owner, or ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER)")
    public ResponseEntity<OrderResponse> getOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(authentication.getName(), orderId));
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "List all orders for an account (owner, or ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER)")
    public ResponseEntity<List<OrderResponse>> getOrdersForAccount(
            Authentication authentication,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(orderService.getOrdersForAccount(authentication.getName(), accountId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEALER','COMPLIANCE_OFFICER','RISK_MANAGER')")
    @Operation(summary = "List all orders across all accounts (ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER only)")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}