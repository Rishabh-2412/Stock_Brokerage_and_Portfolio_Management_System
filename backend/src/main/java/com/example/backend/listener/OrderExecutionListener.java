package com.example.backend.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.example.backend.entity.Order;
import com.example.backend.entity.Account;
import com.example.backend.entity.Holdings;
import com.example.backend.entity.Transaction;
import com.example.backend.entity.Security;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.HoldingsRepository;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.util.AuditLogger;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Component
public class OrderExecutionListener {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HoldingsRepository holdingsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private AuditLogger auditLogger;

    @EventListener
    public void onOrderExecuted(OrderExecutedEvent event) {
        Order order = event.getOrder();
        Account account = order.getAccount();
        Security security = order.getSecurity();

        try {
            if ("BUY".equalsIgnoreCase(order.getOrderType())) {
                handleBuyOrderExecution(order, account, security);
            } else if ("SELL".equalsIgnoreCase(order.getOrderType())) {
                handleSellOrderExecution(order, account, security);
            }

            order.setOrderStatus("completed");
            orderRepository.save(order);
            auditLogger.log("Order executed successfully", "ORDER_EXECUTION", order.getOrderId(), account.getUserId());

        } catch (Exception e) {
            order.setOrderStatus("failed");
            orderRepository.save(order);
            auditLogger.log("Order execution failed: " + e.getMessage(), "ORDER_EXECUTION_ERROR", order.getOrderId(),
                    account.getUserId());
            throw new RuntimeException("Order execution failed", e);
        }
    }

    private void handleBuyOrderExecution(Order order, Account account, Security security) {
        BigDecimal totalCost = order.getPrice().multiply(new BigDecimal(order.getFilledQuantity()));
        BigDecimal commission = calculateCommission(totalCost);
        BigDecimal totalAmount = totalCost.add(commission);

        // Check if account has sufficient funds
        if (account.getCashAvailable().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient funds for buy order execution");
        }

        // Update account balance
        account.setCashAvailable(account.getCashAvailable().subtract(totalAmount));
        account.setBalance(account.getBalance().subtract(totalAmount));
        accountRepository.save(account);

        // Update or create holdings
        Holdings holdings = holdingsRepository.findByAccountAndSecurity(account, security);
        if (holdings != null) {
            BigDecimal totalValue = holdings.getCurrentValue().add(totalCost);
            long newQuantity = holdings.getQuantity() + order.getFilledQuantity();
            BigDecimal newAverageCost = totalValue.divide(new BigDecimal(newQuantity), 2, BigDecimal.ROUND_HALF_UP);

            holdings.setQuantity(newQuantity);
            holdings.setAverageCost(newAverageCost);
            holdings.setCurrentValue(totalCost);
            holdings.setLastUpdated(LocalDateTime.now());
        } else {
            holdings = new Holdings();
            holdings.setAccount(account);
            holdings.setSecurity(security);
            holdings.setQuantity(order.getFilledQuantity());
            holdings.setAverageCost(order.getPrice());
            holdings.setCurrentValue(totalCost);
            holdings.setLastUpdated(LocalDateTime.now());
        }
        holdingsRepository.save(holdings);

        // Create transaction record
        createTransaction(order, account, security, "BUY", order.getPrice(), order.getFilledQuantity(), commission);
    }

    private void handleSellOrderExecution(Order order, Account account, Security security) {
        // Check if account has sufficient holdings
        Holdings holdings = holdingsRepository.findByAccountAndSecurity(account, security);
        if (holdings == null || holdings.getQuantity() < order.getFilledQuantity()) {
            throw new RuntimeException("Insufficient holdings for sell order execution");
        }

        BigDecimal totalProceeds = order.getPrice().multiply(new BigDecimal(order.getFilledQuantity()));
        BigDecimal commission = calculateCommission(totalProceeds);
        BigDecimal netProceeds = totalProceeds.subtract(commission);

        // Update account balance
        account.setCashAvailable(account.getCashAvailable().add(netProceeds));
        account.setBalance(account.getBalance().add(netProceeds));
        accountRepository.save(account);

        // Update holdings
        holdings.setQuantity(holdings.getQuantity() - order.getFilledQuantity());
        holdings.setCurrentValue(holdings.getCurrentValue().subtract(totalProceeds));
        holdings.setLastUpdated(LocalDateTime.now());

        if (holdings.getQuantity() == 0) {
            holdingsRepository.delete(holdings);
        } else {
            holdingsRepository.save(holdings);
        }

        // Create transaction record
        createTransaction(order, account, security, "SELL", order.getPrice(), order.getFilledQuantity(), commission);
    }

    private void createTransaction(Order order, Account account, Security security, String transactionType,
            BigDecimal price, long quantity, BigDecimal commission) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setSecurity(security);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(price.multiply(new BigDecimal(quantity)));
        transaction.setCommission(commission);
        transaction.setStatus("completed");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setSettlementDate(LocalDateTime.now().plusDays(2));

        transactionRepository.save(transaction);
    }

    private BigDecimal calculateCommission(BigDecimal amount) {
        // Commission: 0.1% of transaction amount
        return amount.multiply(new BigDecimal("0.001"));
    }

    @EventListener
    public void onOrderPending(OrderPendingEvent event) {
        Order order = event.getOrder();
        order.setOrderStatus("pending");
        orderRepository.save(order);
        auditLogger.log("Order created and pending", "ORDER_PENDING", order.getOrderId(),
                order.getAccount().getUserId());
    }

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        Order order = event.getOrder();
        Account account = order.getAccount();

        // If it was a buy order with reserved funds, release them
        if ("BUY".equalsIgnoreCase(order.getOrderType()) && "pending".equalsIgnoreCase(order.getOrderStatus())) {
            BigDecimal reservedAmount = order.getPrice().multiply(new BigDecimal(order.getFilledQuantity()));
            account.setCashAvailable(account.getCashAvailable().add(reservedAmount));
            accountRepository.save(account);
        }

        order.setOrderStatus("cancelled");
        orderRepository.save(order);
        auditLogger.log("Order cancelled", "ORDER_CANCELLED", order.getOrderId(), account.getUserId());
    }
}