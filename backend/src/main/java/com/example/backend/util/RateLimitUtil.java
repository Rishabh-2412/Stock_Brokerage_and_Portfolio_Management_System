package com.example.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitUtil {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitUtil.class);

    @Value("${ratelimit.default.requests:100}")
    private int defaultRequests;

    @Value("${ratelimit.default.interval.minutes:1}")
    private int defaultIntervalMinutes;

    @Value("${ratelimit.login.requests:5}")
    private int loginRequests;

    @Value("${ratelimit.login.interval.minutes:5}")
    private int loginIntervalMinutes;

    @Value("${ratelimit.order.requests:50}")
    private int orderRequests;

    @Value("${ratelimit.order.interval.minutes:1}")
    private int orderIntervalMinutes;

    private final ConcurrentHashMap<String, RequestTracker> requestTrackers = new ConcurrentHashMap<>();

    /**
     * Inner class to track requests per identifier
     */
    private static class RequestTracker {
        private long windowStartTime;
        private int requestCount;

        RequestTracker() {
            this.windowStartTime = System.currentTimeMillis();
            this.requestCount = 0;
        }

        synchronized boolean isWindowExpired(long windowDurationMs) {
            return System.currentTimeMillis() - windowStartTime > windowDurationMs;
        }

        synchronized void resetWindow() {
            this.windowStartTime = System.currentTimeMillis();
            this.requestCount = 0;
        }

        synchronized void incrementCount() {
            this.requestCount++;
        }

        synchronized int getCount() {
            return this.requestCount;
        }
    }

    /**
     * Check if request is allowed for general API endpoints
     */
    public boolean isAllowedGeneralRequest(String identifier) {
        return checkRateLimit(identifier, defaultRequests, defaultIntervalMinutes);
    }

    /**
     * Check if request is allowed for login endpoint
     */
    public boolean isAllowedLoginRequest(String identifier) {
        return checkRateLimit(identifier, loginRequests, loginIntervalMinutes);
    }

    /**
     * Check if request is allowed for order endpoint
     */
    public boolean isAllowedOrderRequest(String identifier) {
        return checkRateLimit(identifier, orderRequests, orderIntervalMinutes);
    }

    /**
     * Check if request is allowed with custom limits
     */
    public boolean isAllowed(String identifier, int maxRequests, int intervalMinutes) {
        return checkRateLimit(identifier, maxRequests, intervalMinutes);
    }

    /**
     * Core rate limit check logic
     */
    private boolean checkRateLimit(String identifier, int maxRequests, int intervalMinutes) {
        if (identifier == null || identifier.trim().isEmpty()) {
            logger.warn("Rate limit check failed: null or empty identifier");
            return false;
        }

        long windowDurationMs = TimeUnit.MINUTES.toMillis(intervalMinutes);

        RequestTracker tracker = requestTrackers.computeIfAbsent(identifier, k -> new RequestTracker());

        // Check if current window has expired
        if (tracker.isWindowExpired(windowDurationMs)) {
            tracker.resetWindow();
        }

        // Check if request count exceeds limit
        if (tracker.getCount() >= maxRequests) {
            logger.warn("Rate limit exceeded for identifier: {}", identifier);
            return false;
        }

        // Increment count and allow request
        tracker.incrementCount();
        return true;
    }

    /**
     * Get remaining requests for identifier
     */
    public int getRemainingRequests(String identifier, int maxRequests, int intervalMinutes) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return maxRequests;
        }

        long windowDurationMs = TimeUnit.MINUTES.toMillis(intervalMinutes);
        RequestTracker tracker = requestTrackers.get(identifier);

        if (tracker == null) {
            return maxRequests;
        }

        if (tracker.isWindowExpired(windowDurationMs)) {
            return maxRequests;
        }

        return Math.max(0, maxRequests - tracker.getCount());
    }

    /**
     * Get time until window reset in seconds
     */
    public long getTimeUntilReset(String identifier, int intervalMinutes) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return 0;
        }

        RequestTracker tracker = requestTrackers.get(identifier);

        if (tracker == null) {
            return 0;
        }

        long windowDurationMs = TimeUnit.MINUTES.toMillis(intervalMinutes);
        long elapsedMs = System.currentTimeMillis() - tracker.windowStartTime;
        long remainingMs = Math.max(0, windowDurationMs - elapsedMs);

        return TimeUnit.MILLISECONDS.toSeconds(remainingMs);
    }

    /**
     * Reset rate limit for identifier
     */
    public void resetRateLimit(String identifier) {
        if (identifier != null && !identifier.trim().isEmpty()) {
            RequestTracker tracker = requestTrackers.get(identifier);
            if (tracker != null) {
                tracker.resetWindow();
                logger.info("Rate limit reset for identifier: {}", identifier);
            }
        }
    }

    /**
     * Remove identifier from tracking (cleanup)
     */
    public void removeIdentifier(String identifier) {
        if (identifier != null && !identifier.trim().isEmpty()) {
            requestTrackers.remove(identifier);
            logger.info("Removed rate limit tracking for identifier: {}", identifier);
        }
    }

    /**
     * Clear all rate limit data
     */
    public void clearAllLimits() {
        requestTrackers.clear();
        logger.info("Cleared all rate limit data");
    }

    /**
     * Get current request count for identifier
     */
    public int getCurrentRequestCount(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return 0;
        }

        RequestTracker tracker = requestTrackers.get(identifier);
        return tracker != null ? tracker.getCount() : 0;
    }

    /**
     * Check if identifier is being tracked
     */
    public boolean isTracked(String identifier) {
        return identifier != null && requestTrackers.containsKey(identifier);
    }
}