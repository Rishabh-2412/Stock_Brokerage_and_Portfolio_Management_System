package com.example.backend.util;

public class Constants {

    // ==================== HTTP Status Codes ====================
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_SERVICE_UNAVAILABLE = 503;

    // ==================== User Account Types ====================
    public static final String ACCOUNT_TYPE_INDIVIDUAL = "individual";
    public static final String ACCOUNT_TYPE_INSTITUTIONAL = "institutional";

    // ==================== Trading Account Types ====================
    public static final String TRADING_ACCOUNT_CASH = "cash";
    public static final String TRADING_ACCOUNT_MARGIN = "margin";
    public static final String TRADING_ACCOUNT_DEMO = "demo";

    // ==================== Account Status ====================
    public static final String ACCOUNT_STATUS_ACTIVE = "active";
    public static final String ACCOUNT_STATUS_INACTIVE = "inactive";
    public static final String ACCOUNT_STATUS_SUSPENDED = "suspended";

    // ==================== KYC Status ====================
    public static final String KYC_STATUS_PENDING = "pending";
    public static final String KYC_STATUS_APPROVED = "approved";
    public static final String KYC_STATUS_REJECTED = "rejected";

    // ==================== Order Types ====================
    public static final String ORDER_TYPE_BUY = "buy";
    public static final String ORDER_TYPE_SELL = "sell";

    // ==================== Order Status ====================
    public static final String ORDER_STATUS_PENDING = "pending";
    public static final String ORDER_STATUS_MARKET = "market";
    public static final String ORDER_STATUS_LIMIT = "limit";
    public static final String ORDER_STATUS_STOP = "stop";

    // ==================== Transaction Types ====================
    public static final String TRANSACTION_TYPE_BUY = "buy";
    public static final String TRANSACTION_TYPE_SELL = "sell";
    public static final String TRANSACTION_TYPE_DIVIDEND = "dividend";

    // ==================== Transaction Status ====================
    public static final String TRANSACTION_STATUS_PENDING = "pending";
    public static final String TRANSACTION_STATUS_COMPLETED = "completed";
    public static final String TRANSACTION_STATUS_FAILED = "failed";
    public static final String TRANSACTION_STATUS_CANCELLED = "cancelled";

    // ==================== Password Requirements ====================
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 128;

    // ==================== Username Requirements ====================
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;

    // ==================== Email Requirements ====================
    public static final int MAX_EMAIL_LENGTH = 100;

    // ==================== Full Name Requirements ====================
    public static final int MIN_FULLNAME_LENGTH = 3;
    public static final int MAX_FULLNAME_LENGTH = 100;

    // ==================== Commission & Fees ====================
    public static final double DEFAULT_COMMISSION_PERCENTAGE = 0.1;
    public static final double MARGIN_REQUIREMENT_PERCENTAGE = 30.0;
    public static final double MARGIN_INTEREST_RATE = 0.02;
    public static final double DIVIDEND_TAX_RATE = 0.0;

    // ==================== Validation Patterns ====================
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    public static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
    public static final String IFSC_REGEX = "^[A-Z]{4}0[A-Z0-9]{6}$";

    // ==================== Security & JWT ====================
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long JWT_EXPIRATION_MS = 3600000; // 1 hour
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 604800000; // 7 days
    public static final String JWT_CLAIM_USER_ID = "userId";
    public static final String JWT_CLAIM_USERNAME = "username";
    public static final String JWT_CLAIM_EMAIL = "email";
    public static final String JWT_CLAIM_ACCOUNT_TYPE = "accountType";

    // ==================== Rate Limiting ====================
    public static final int DEFAULT_RATE_LIMIT_REQUESTS = 100;
    public static final int DEFAULT_RATE_LIMIT_WINDOW_MINUTES = 1;
    public static final int LOGIN_RATE_LIMIT_REQUESTS = 5;
    public static final int LOGIN_RATE_LIMIT_WINDOW_MINUTES = 15;
    public static final int ORDER_RATE_LIMIT_REQUESTS = 50;
    public static final int ORDER_RATE_LIMIT_WINDOW_MINUTES = 1;

    // ==================== Pagination ====================
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    // ==================== API Endpoints ====================
    public static final String API_BASE_PATH = "/api/v1";
    public static final String AUTH_ENDPOINT = "/auth";
    public static final String USERS_ENDPOINT = "/users";
    public static final String ACCOUNTS_ENDPOINT = "/accounts";
    public static final String ORDERS_ENDPOINT = "/orders";
    public static final String PORTFOLIO_ENDPOINT = "/portfolio";
    public static final String TRANSACTIONS_ENDPOINT = "/transactions";
    public static final String SECURITIES_ENDPOINT = "/securities";
    public static final String WATCHLIST_ENDPOINT = "/watchlist";
    public static final String MARKET_ENDPOINT = "/market";

    // ==================== Error Messages ====================
    public static final String ERROR_INVALID_EMAIL = "Invalid email format";
    public static final String ERROR_INVALID_USERNAME = "Invalid username format";
    public static final String ERROR_INVALID_PASSWORD = "Password does not meet requirements";
    public static final String ERROR_INVALID_PHONE = "Invalid phone number";
    public static final String ERROR_INVALID_FULLNAME = "Invalid full name";
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found";
    public static final String ERROR_SECURITY_NOT_FOUND = "Security not found";
    public static final String ERROR_ORDER_NOT_FOUND = "Order not found";
    public static final String ERROR_TRANSACTION_NOT_FOUND = "Transaction not found";
    public static final String ERROR_INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String ERROR_INSUFFICIENT_QUANTITY = "Insufficient quantity";
    public static final String ERROR_INSUFFICIENT_MARGIN = "Insufficient margin";
    public static final String ERROR_DUPLICATE_USERNAME = "Username already exists";
    public static final String ERROR_DUPLICATE_EMAIL = "Email already exists";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_UNAUTHORIZED_ACCESS = "Unauthorized access";
    public static final String ERROR_ACCOUNT_SUSPENDED = "Account is suspended";
    public static final String ERROR_KYC_NOT_APPROVED = "KYC not approved";
    public static final String ERROR_INVALID_ACCOUNT_TYPE = "Invalid account type";
    public static final String ERROR_INVALID_ORDER_TYPE = "Invalid order type";
    public static final String ERROR_INVALID_QUANTITY = "Invalid quantity";
    public static final String ERROR_INVALID_PRICE = "Invalid price";
    public static final String ERROR_ORDER_ALREADY_FILLED = "Order already filled";
    public static final String ERROR_CANNOT_CANCEL_ORDER = "Cannot cancel this order";
    public static final String ERROR_INVALID_STATUS_TRANSITION = "Invalid status transition";
    public static final String ERROR_DUPLICATE_TRADE = "Duplicate trade detected";
    public static final String ERROR_INTERNAL_SERVER = "Internal server error";
    public static final String ERROR_RATE_LIMIT_EXCEEDED = "Rate limit exceeded";

    // ==================== Success Messages ====================
    public static final String SUCCESS_USER_CREATED = "User created successfully";
    public static final String SUCCESS_USER_UPDATED = "User updated successfully";
    public static final String SUCCESS_LOGIN_SUCCESSFUL = "Login successful";
    public static final String SUCCESS_LOGOUT_SUCCESSFUL = "Logout successful";
    public static final String SUCCESS_ACCOUNT_CREATED = "Account created successfully";
    public static final String SUCCESS_ORDER_PLACED = "Order placed successfully";
    public static final String SUCCESS_ORDER_CANCELLED = "Order cancelled successfully";
    public static final String SUCCESS_TRANSACTION_COMPLETED = "Transaction completed successfully";
    public static final String SUCCESS_PASSWORD_CHANGED = "Password changed successfully";
    public static final String SUCCESS_PROFILE_UPDATED = "Profile updated successfully";
    public static final String SUCCESS_WATCHLIST_UPDATED = "Watchlist updated successfully";

    // ==================== Transaction Limits ====================
    public static final double MIN_TRANSACTION_AMOUNT = 100.0;
    public static final double MAX_TRANSACTION_AMOUNT = 10000000.0;
    public static final int MIN_ORDER_QUANTITY = 1;
    public static final int MAX_ORDER_QUANTITY = 1000000;

    // ==================== Cache TTL (Time To Live) ====================
    public static final long CACHE_TTL_SECURITIES = 300; // 5 minutes
    public static final long CACHE_TTL_PRICE_HISTORY = 60; // 1 minute
    public static final long CACHE_TTL_PORTFOLIO = 30; // 30 seconds
    public static final long CACHE_TTL_USER = 600; // 10 minutes

    // ==================== Date & Time Formats ====================
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";

    // ==================== Exchange Hours ====================
    public static final String MARKET_OPEN_TIME = "09:15";
    public static final String MARKET_CLOSE_TIME = "15:30";
    public static final String MARKET_PREOPEN_TIME = "09:00";

    // ==================== Audit Log Events ====================
    public static final String AUDIT_EVENT_LOGIN = "LOGIN";
    public static final String AUDIT_EVENT_LOGOUT = "LOGOUT";
    public static final String AUDIT_EVENT_ORDER_PLACED = "ORDER_PLACED";
    public static final String AUDIT_EVENT_ORDER_CANCELLED = "ORDER_CANCELLED";
    public static final String AUDIT_EVENT_TRANSACTION = "TRANSACTION";
    public static final String AUDIT_EVENT_SECURITY_ALERT = "SECURITY_ALERT";

    // ==================== Default Values ====================
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_DELAY_MS = 1000;

    // ==================== Token Claims ====================
    public static final String TOKEN_CLAIM_SUB = "sub";
    public static final String TOKEN_CLAIM_IAT = "iat";
    public static final String TOKEN_CLAIM_EXP = "exp";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }
}