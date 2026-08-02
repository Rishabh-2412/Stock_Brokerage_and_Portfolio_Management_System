package com.example.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOG");

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Event types
    public static final String EVENT_LOGIN = "LOGIN";
    public static final String EVENT_LOGOUT = "LOGOUT";
    public static final String EVENT_REGISTRATION = "REGISTRATION";
    public static final String EVENT_ORDER_PLACED = "ORDER_PLACED";
    public static final String EVENT_ORDER_CANCELLED = "ORDER_CANCELLED";
    public static final String EVENT_ORDER_EXECUTED = "ORDER_EXECUTED";
    public static final String EVENT_TRANSACTION = "TRANSACTION";
    public static final String EVENT_FUND_TRANSFER = "FUND_TRANSFER";
    public static final String EVENT_KYC_SUBMITTED = "KYC_SUBMITTED";
    public static final String EVENT_KYC_APPROVED = "KYC_APPROVED";
    public static final String EVENT_KYC_REJECTED = "KYC_REJECTED";
    public static final String EVENT_ACCOUNT_CREATED = "ACCOUNT_CREATED";
    public static final String EVENT_ACCOUNT_SUSPENDED = "ACCOUNT_SUSPENDED";
    public static final String EVENT_PASSWORD_CHANGED = "PASSWORD_CHANGED";
    public static final String EVENT_PROFILE_UPDATED = "PROFILE_UPDATED";
    public static final String EVENT_WATCHLIST_UPDATED = "WATCHLIST_UPDATED";
    public static final String EVENT_SECURITY_ALERT = "SECURITY_ALERT";
    public static final String EVENT_API_ERROR = "API_ERROR";

    // Status types
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILURE = "FAILURE";
    public static final String STATUS_PENDING = "PENDING";

    /**
     * Log user action
     */
    public void logUserAction(String userId, String eventType, String action, String status) {
        logUserAction(userId, eventType, action, status, null);
    }

    /**
     * Log user action with details
     */
    public void logUserAction(String userId, String eventType, String action, String status, String details) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("userId", userId);
        auditData.put("eventType", eventType);
        auditData.put("action", action);
        auditData.put("status", status);
        if (details != null) {
            auditData.put("details", details);
        }

        auditLogger.info("{}", formatAuditEntry(auditData));
        logger.debug("Audit log: userId={}, eventType={}, action={}, status={}", 
                    userId, eventType, action, status);
    }

    /**
     * Log transaction
     */
    public void logTransaction(String userId, String accountId, String transactionType, 
                               double amount, String symbol, String status) {
        logTransaction(userId, accountId, transactionType, amount, symbol, status, null);
    }

    /**
     * Log transaction with details
     */
    public void logTransaction(String userId, String accountId, String transactionType, 
                               double amount, String symbol, String status, String details) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("userId", userId);
        auditData.put("accountId", accountId);
        auditData.put("eventType", EVENT_TRANSACTION);
        auditData.put("transactionType", transactionType);
        auditData.put("amount", amount);
        auditData.put("symbol", symbol);
        auditData.put("status", status);
        if (details != null) {
            auditData.put("details", details);
        }

        auditLogger.info("{}", formatAuditEntry(auditData));
        logger.debug("Transaction logged: userId={}, transactionType={}, amount={}, status={}", 
                    userId, transactionType, amount, status);
    }

    /**
     * Log order
     */
    public void logOrder(String userId, String accountId, String orderId, String orderType, 
                         String symbol, int quantity, double price, String status) {
        logOrder(userId, accountId, orderId, orderType, symbol, quantity, price, status, null);
    }

    /**
     * Log order with details
     */
    public void logOrder(String userId, String accountId, String orderId, String orderType, 
                         String symbol, int quantity, double price, String status, String details) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("userId", userId);
        auditData.put("accountId", accountId);
        auditData.put("orderId", orderId);
        auditData.put("orderType", orderType);
        auditData.put("symbol", symbol);
        auditData.put("quantity", quantity);
        auditData.put("price", price);
        auditData.put("status", status);
        if (details != null) {
            auditData.put("details", details);
        }

        auditLogger.info("{}", formatAuditEntry(auditData));
        logger.debug("Order logged: orderId={}, orderType={}, symbol={}, status={}", 
                    orderId, orderType, symbol, status);
    }

    /**
     * Log security alert
     */
    public void logSecurityAlert(String userId, String alertType, String description) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("userId", userId);
        auditData.put("eventType", EVENT_SECURITY_ALERT);
        auditData.put("alertType", alertType);
        auditData.put("description", description);
        auditData.put("status", STATUS_FAILURE);

        auditLogger.warn("{}", formatAuditEntry(auditData));
        logger.warn("Security alert: userId={}, alertType={}, description={}", 
                   userId, alertType, description);
    }

    /**
     * Log API error
     */
    public void logApiError(String userId, String endpoint, String method, 
                            String errorMessage, int statusCode) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        if (userId != null) {
            auditData.put("userId", userId);
        }
        auditData.put("eventType", EVENT_API_ERROR);
        auditData.put("endpoint", endpoint);
        auditData.put("method", method);
        auditData.put("errorMessage", errorMessage);
        auditData.put("statusCode", statusCode);
        auditData.put("status", STATUS_FAILURE);

        auditLogger.error("{}", formatAuditEntry(auditData));
        logger.error("API error: endpoint={}, method={}, statusCode={}, error={}", 
                    endpoint, method, statusCode, errorMessage);
    }

    /**
     * Log authentication attempt
     */
    public void logAuthenticationAttempt(String username, String ipAddress, boolean success) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("username", username);
        auditData.put("ipAddress", ipAddress);
        auditData.put("eventType", EVENT_LOGIN);
        auditData.put("status", success ? STATUS_SUCCESS : STATUS_FAILURE);

        auditLogger.info("{}", formatAuditEntry(auditData));
        logger.info("Authentication attempt: username={}, success={}", username, success);
    }

    /**
     * Log account state change
     */
    public void logAccountStateChange(String userId, String accountId, String fromStatus, 
                                      String toStatus, String reason) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("userId", userId);
        auditData.put("accountId", accountId);
        auditData.put("eventType", "ACCOUNT_STATE_CHANGE");
        auditData.put("fromStatus", fromStatus);
        auditData.put("toStatus", toStatus);
        auditData.put("reason", reason);
        auditData.put("status", STATUS_SUCCESS);

        auditLogger.info("{}", formatAuditEntry(auditData));
        logger.info("Account state changed: accountId={}, from={}, to={}", 
                   accountId, fromStatus, toStatus);
    }

    /**
     * Log data access
     */
    public void logDataAccess(String userId, String resourceType, String resourceId, String action) {
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(formatter));
        auditData.put("userId", userId);
        auditData.put("eventType", "DATA_ACCESS");
        auditData.put("resourceType", resourceType);
        auditData.put("resourceId", resourceId);
        auditData.put("action", action);
        auditData.put("status", STATUS_SUCCESS);

        auditLogger.debug("{}", formatAuditEntry(auditData));
        logger.debug("Data accessed: userId={}, resourceType={}, action={}", 
                    userId, resourceType, action);
    }

    /**
     * Format audit entry as string
     */
    private String formatAuditEntry(Map<String, Object> auditData) {
        StringBuilder sb = new StringBuilder();
        sb.append("[AUDIT] ");
        
        auditData.forEach((key, value) -> {
            if (value != null) {
                sb.append(key).append("=").append(value).append(" | ");
            }
        });

        // Remove trailing " | "
        if (sb.length() > 8) {
            sb.setLength(sb.length() - 3);
        }

        return sb.toString();
    }

    /**
     * Log generic message
     */
    public void log(String eventType, String message) {
        auditLogger.info("[{}] {}", eventType, message);
        logger.info("Event: {}, Message: {}", eventType, message);
    }
}