package com.example.backend.service.impl;

import com.example.backend.dto.request.PlaceOrderRequest;
import com.example.backend.dto.response.OrderResponse;
import com.example.backend.entity.Account;
import com.example.backend.entity.Order;
import com.example.backend.entity.Security;
import com.example.backend.entity.User;
import com.example.backend.entity.enums.AccountStatus;
import com.example.backend.entity.enums.OrderStatus;
import com.example.backend.entity.enums.OrderType;
import com.example.backend.entity.enums.PriceType;
import com.example.backend.entity.enums.Role;
import com.example.backend.exception.InsufficientMarginException;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorisedAccessException;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Placement-only: validates and records the order as PENDING. Does NOT
 * deduct cash, does NOT touch Holdings, does NOT actually fill the order -
 * that's the separate Order Execution module (next).
 *
 * Roles:
 * - CLIENT can place/cancel orders only on accounts they own.
 * - DEALER and ADMIN can place/cancel orders on ANY account (trading on
 *   behalf of a client).
 * - ADMIN, DEALER, COMPLIANCE_OFFICER, RISK_MANAGER can view ANY order.
 * - RESEARCH_ANALYST has no access here (not part of the order flow).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Set<Role> UNRESTRICTED_TRADE_ROLES = Set.of(Role.DEALER, Role.ADMIN);
    private static final Set<Role> READ_ALL_ROLES = Set.of(
            Role.ADMIN, Role.DEALER, Role.COMPLIANCE_OFFICER, Role.RISK_MANAGER
    );

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final SecurityRepository securityRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse placeOrder(String username, PlaceOrderRequest request) {
        User user = getUserOrThrow(username);
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + request.getAccountId()));

        assertCanTradeOnAccount(user, account);

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOrderException("Account is not active: " + account.getStatus());
        }

        Security security = securityRepository.findById(request.getSecurityId())
                .orElseThrow(() -> new ResourceNotFoundException("Security not found: " + request.getSecurityId()));

        if (request.getPriceType() == PriceType.LIMIT && request.getPrice() == null) {
            throw new InvalidOrderException("price is required for LIMIT orders");
        }

        BigDecimal effectivePrice = request.getPriceType() == PriceType.LIMIT
                ? request.getPrice()
                : security.getCurrentPrice();

        if (request.getOrderType() == OrderType.BUY) {
            BigDecimal estimatedCost = effectivePrice.multiply(BigDecimal.valueOf(request.getQuantity()));
            if (estimatedCost.compareTo(account.getCashAvailable()) > 0) {
                throw new InsufficientMarginException(
                        "Insufficient funds: order needs " + estimatedCost
                                + " but only " + account.getCashAvailable() + " is available");
            }
        }
        // NOTE: SELL orders are not checked against Holdings yet - Holdings
        // doesn't exist until Module 6/7. Add that check once Holdings exists.

        Order order = Order.builder()
                .account(account)
                .security(security)
                .orderType(request.getOrderType())
                .priceType(request.getPriceType())
                .orderStatus(OrderStatus.PENDING)
                .quantity(request.getQuantity())
                .price(effectivePrice)
                .filledQuantity(0)
                .build();

        return OrderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse cancelOrder(String username, Long orderId) {
        User user = getUserOrThrow(username);
        Order order = getOrderOrThrow(orderId);

        assertCanTradeOnAccount(user, order.getAccount());

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderException("Only PENDING orders can be cancelled (current status: "
                    + order.getOrderStatus() + ")");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        return OrderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String username, Long orderId) {
        User user = getUserOrThrow(username);
        Order order = getOrderOrThrow(orderId);
        assertCanViewAccount(user, order.getAccount());
        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersForAccount(String username, Long accountId) {
        User user = getUserOrThrow(username);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        assertCanViewAccount(user, account);

        return orderRepository.findByAccountId(accountId)
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void assertCanTradeOnAccount(User user, Account account) {
        boolean isOwner = account.getUser().getId().equals(user.getId());
        boolean isUnrestricted = UNRESTRICTED_TRADE_ROLES.contains(user.getRole());
        if (!isOwner && !isUnrestricted) {
            throw new UnauthorisedAccessException("You do not have permission to trade on this account");
        }
    }

    private void assertCanViewAccount(User user, Account account) {
        boolean isOwner = account.getUser().getId().equals(user.getId());
        boolean canReadAny = READ_ALL_ROLES.contains(user.getRole());
        if (!isOwner && !canReadAny) {
            throw new UnauthorisedAccessException("You do not have access to this account's orders");
        }
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }
}