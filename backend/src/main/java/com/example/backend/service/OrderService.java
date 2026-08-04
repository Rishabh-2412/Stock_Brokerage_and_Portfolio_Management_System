package com.example.backend.service;

import com.example.backend.dto.OrderDTO;
import com.example.backend.dto.request.PlaceOrderRequest;
import com.example.backend.dto.response.OrderResponse;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

public interface OrderService {

    /**
     * Place new order with validations (margin check, quantity, price)
     * @param accountId account identifier
     * @param placeOrderRequest order details (symbol, quantity, price, type)
     * @return OrderResponse with order details and status
     */
    OrderResponse placeOrder(Long accountId, PlaceOrderRequest placeOrderRequest);

    /**
     * Get order by order ID
     * @param orderId order identifier
     * @return OrderDTO with order details
     */
    OrderDTO getOrderById(Long orderId);

    /**
     * Get all orders for an account
     * @param accountId account identifier
     * @return List of OrderDTOs
     */
    List<OrderDTO> getOrdersByAccount(Long accountId);

    /**
     * Get all pending orders for an account
     * @param accountId account identifier
     * @return List of pending OrderDTOs
     */
    List<OrderDTO> getPendingOrdersByAccount(Long accountId);

    /**
     * Get all executed orders for an account
     * @param accountId account identifier
     * @return List of executed OrderDTOs
     */
    List<OrderDTO> getExecutedOrdersByAccount(Long accountId);

    /**
     * Cancel pending order (immutable once executed)
     * @param orderId order identifier
     * @return true if cancellation successful
     */
    boolean cancelOrder(Long orderId);

    /**
     * Execute order at market price
     * @param orderId order identifier
     * @param executionPrice price at execution
     * @return true if execution successful
     */
    boolean executeOrder(Long orderId, BigDecimal executionPrice);

    /**
     * Get order status
     * @param orderId order identifier
     * @return order status (pending/market/limit/stop/filled/cancelled)
     */
    String getOrderStatus(Long orderId);

    /**
     * Get filled quantity for partial fills
     * @param orderId order identifier
     * @return quantity filled
     */
    Long getFilledQuantity(Long orderId);

    /**
     * Update order status
     * @param orderId order identifier
     * @param newStatus new order status
     */
    void updateOrderStatus(Long orderId, String newStatus);

    /**
     * Get orders for date range
     * @param accountId account identifier
     * @param startDate start date
     * @param endDate end date
     * @return List of OrderDTOs in date range
     */
    List<OrderDTO> getOrdersByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get today's complete order book
     * @return List of all orders placed today across all accounts
     */
    List<OrderDTO> getTodayOrderBook();

    /**
     * Process stop-loss order at trigger price during market hours
     * @param orderId order identifier
     * @param triggerPrice stop-loss trigger price
     */
    void processStopLossOrder(Long orderId, BigDecimal triggerPrice);

    /**
     * Validate order before placement (margin, quantity, lot size, position limits)
     * @param accountId account identifier
     * @param placeOrderRequest order details
     * @return true if order is valid
     */
    boolean validateOrder(Long accountId, PlaceOrderRequest placeOrderRequest);

    /**
     * Check position limits (SEBI client-level concentration norms)
     * @param accountId account identifier
     * @param symbol security symbol
     * @param quantity order quantity
     * @return true if position limits are not exceeded
     */
    boolean checkPositionLimits(Long accountId, String symbol, Long quantity);

    /**
     * Calculate required margin for order
     * @param symbol security symbol
     * @param quantity order quantity
     * @param price order price
     * @return required margin amount
     */
    BigDecimal calculateRequiredMargin(String symbol, Long quantity, BigDecimal price);

    /**
     * Get order history with pagination
     * @param accountId account identifier
     * @param pageNumber page index
     * @param pageSize number of records per page
     * @return List of OrderDTOs
     */
    List<OrderDTO> getOrderHistory(Long accountId, int pageNumber, int pageSize);

    /**
     * Queue post-market order for next trading session
     * @param orderId order identifier
     */
    void queuePostMarketOrder(Long orderId);

    /**
     * Execute queued post-market orders at market open
     * @param openPrice market open price
     */
    void executePostMarketOrders(BigDecimal openPrice);
}