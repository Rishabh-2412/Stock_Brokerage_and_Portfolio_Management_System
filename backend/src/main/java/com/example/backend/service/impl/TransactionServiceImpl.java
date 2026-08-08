package com.example.backend.service.impl;

import com.example.backend.dto.TransactionDTO;
import com.example.backend.entity.Account;
import com.example.backend.entity.Holdings;
import com.example.backend.entity.Order;
import com.example.backend.entity.Security;
import com.example.backend.entity.Transaction;
import com.example.backend.entity.User;
import com.example.backend.entity.enums.OrderStatus;
import com.example.backend.entity.enums.OrderType;
import com.example.backend.entity.enums.Role;
import com.example.backend.entity.enums.TransactionStatus;
import com.example.backend.entity.enums.TransactionType;
import com.example.backend.exception.InsufficientMarginException;
import com.example.backend.exception.InvalidOrderException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorisedAccessException;
import com.example.backend.mapper.TransactionMapper;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.HoldingsRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UPDATED: getTransactionsForAccount now takes optional type/startDate/
 * endDate filters (pass null to skip any of them). Filtering happens
 * in-memory via streams after fetching by account - fine for mini-project
 * data volumes; swap for a JPA query with dynamic WHERE clauses (e.g.
 * Spring Data Specifications) if this ever needs to scale.
 *
 * Everything else in this class is unchanged from the previous version.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.001"); // 0.1% flat, mini-project simplification
    private static final Set<Role> UNRESTRICTED_TRADE_ROLES = Set.of(Role.DEALER, Role.ADMIN);
    private static final Set<Role> READ_ALL_ROLES = Set.of(
            Role.ADMIN, Role.DEALER, Role.COMPLIANCE_OFFICER, Role.RISK_MANAGER
    );

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final HoldingsRepository holdingsRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public TransactionDTO executeOrder(String username, Long orderId) {
        User user = getUserOrThrow(username);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        Account account = order.getAccount();
        assertCanTradeOnAccount(user, account);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderException("Only PENDING orders can be executed (current status: "
                    + order.getOrderStatus() + ")");
        }

        Security security = order.getSecurity();
        BigDecimal price = order.getPrice();
        int quantity = order.getQuantity();

        BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal commission = totalAmount.multiply(COMMISSION_RATE).setScale(4, RoundingMode.HALF_UP);

        if (order.getOrderType() == OrderType.BUY) {
            executeBuy(account, security, quantity, price, totalAmount, commission);
        } else {
            executeSell(account, security, quantity, price, totalAmount, commission);
        }

        accountRepository.save(account);

        order.setFilledQuantity(quantity);
        order.setOrderStatus(OrderStatus.FILLED);
        orderRepository.save(order);

        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = Transaction.builder()
                .account(account)
                .security(security)
                .transactionType(order.getOrderType() == OrderType.BUY ? TransactionType.BUY : TransactionType.SELL)
                .quantity(quantity)
                .price(price)
                .totalAmount(totalAmount)
                .commission(commission)
                .status(TransactionStatus.COMPLETED)
                .transactionDate(now)
                .settlementDate(now)
                .build();

        return TransactionMapper.toDTO(transactionRepository.save(transaction));
    }

    private void executeBuy(Account account, Security security, int quantity, BigDecimal price,
                             BigDecimal totalAmount, BigDecimal commission) {
        BigDecimal totalCost = totalAmount.add(commission);

        if (totalCost.compareTo(account.getCashAvailable()) > 0) {
            throw new InsufficientMarginException(
                    "Insufficient funds to execute: needs " + totalCost
                            + " but only " + account.getCashAvailable() + " is available");
        }

        account.setCashAvailable(account.getCashAvailable().subtract(totalCost));
        account.setBalance(account.getBalance().subtract(totalCost));

        Holdings holding = holdingsRepository.findByAccountIdAndSecurityId(account.getId(), security.getId())
                .orElse(null);

        if (holding == null) {
            holding = Holdings.builder()
                    .account(account)
                    .security(security)
                    .quantity(quantity)
                    .averageCost(price)
                    .build();
        } else {
            int newQuantity = holding.getQuantity() + quantity;
            BigDecimal existingTotalCost = holding.getAverageCost().multiply(BigDecimal.valueOf(holding.getQuantity()));
            BigDecimal newTotalCost = existingTotalCost.add(totalAmount);
            BigDecimal newAverageCost = newTotalCost.divide(BigDecimal.valueOf(newQuantity), 4, RoundingMode.HALF_UP);

            holding.setQuantity(newQuantity);
            holding.setAverageCost(newAverageCost);
        }

        holding.setCurrentValue(security.getCurrentPrice().multiply(BigDecimal.valueOf(holding.getQuantity())));
        holding.setLastUpdated(LocalDateTime.now());
        holdingsRepository.save(holding);
    }

    private void executeSell(Account account, Security security, int quantity, BigDecimal price,
                              BigDecimal totalAmount, BigDecimal commission) {
        Holdings holding = holdingsRepository.findByAccountIdAndSecurityId(account.getId(), security.getId())
                .orElseThrow(() -> new InvalidOrderException(
                        "Cannot sell " + security.getSymbol() + " - no holding found for this account"));

        if (holding.getQuantity() < quantity) {
            throw new InvalidOrderException(
                    "Cannot sell " + quantity + " shares of " + security.getSymbol()
                            + " - only " + holding.getQuantity() + " held");
        }

        int remainingQuantity = holding.getQuantity() - quantity;
        if (remainingQuantity == 0) {
            holdingsRepository.delete(holding);
        } else {
            holding.setQuantity(remainingQuantity);
            holding.setCurrentValue(security.getCurrentPrice().multiply(BigDecimal.valueOf(remainingQuantity)));
            holding.setLastUpdated(LocalDateTime.now());
            holdingsRepository.save(holding);
        }

        BigDecimal proceeds = totalAmount.subtract(commission);
        account.setCashAvailable(account.getCashAvailable().add(proceeds));
        account.setBalance(account.getBalance().add(proceeds));
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(String username, Long transactionId) {
        User user = getUserOrThrow(username);
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));

        assertCanViewAccount(user, transaction.getAccount());
        return TransactionMapper.toDTO(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsForAccount(String username, Long accountId,
                                                            TransactionType type,
                                                            LocalDate startDate,
                                                            LocalDate endDate) {
        User user = getUserOrThrow(username);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        assertCanViewAccount(user, account);

        LocalDateTime startBound = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endBound = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        return transactionRepository.findByAccountId(accountId)
                .stream()
                .filter(t -> type == null || t.getTransactionType() == type)
                .filter(t -> startBound == null || !t.getTransactionDate().isBefore(startBound))
                .filter(t -> endBound == null || !t.getTransactionDate().isAfter(endBound))
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void assertCanTradeOnAccount(User user, Account account) {
        boolean isOwner = account.getUser().getId().equals(user.getId());
        boolean isUnrestricted = UNRESTRICTED_TRADE_ROLES.contains(user.getRole());
        if (!isOwner && !isUnrestricted) {
            throw new UnauthorisedAccessException("You do not have permission to execute orders on this account");
        }
    }

    private void assertCanViewAccount(User user, Account account) {
        boolean isOwner = account.getUser().getId().equals(user.getId());
        boolean canReadAny = READ_ALL_ROLES.contains(user.getRole());
        if (!isOwner && !canReadAny) {
            throw new UnauthorisedAccessException("You do not have access to this account's transactions");
        }
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}