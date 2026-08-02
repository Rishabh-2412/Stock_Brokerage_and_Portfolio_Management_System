package com.example.backend.security;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    // Maps token to expiration time for automatic cleanup
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    // For In-Memory Implementation
    // Note: In production, consider using Redis for distributed systems

    /**
     * Add token to blacklist (typically on logout)
     */
    public void blacklistToken(String token, long expirationTime) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.put(token, expirationTime);
            // Optional: Schedule cleanup after token expiration
            scheduleTokenCleanup(token, expirationTime);
        }
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        Long expirationTime = blacklistedTokens.get(token);

        if (expirationTime == null) {
            return false;
        }

        // If token expiration time has passed, remove it and return false
        if (expirationTime < System.currentTimeMillis()) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }

    /**
     * Remove token from blacklist
     */
    public void removeToken(String token) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.remove(token);
        }
    }

    /**
     * Get all blacklisted tokens (for monitoring/debugging)
     */
    public Set<String> getAllBlacklistedTokens() {
        return new HashSet<>(blacklistedTokens.keySet());
    }

    /**
     * Clear all expired tokens from blacklist
     */
    public void clearExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }

    /**
     * Schedule automatic cleanup of token after its expiration
     */
    private void scheduleTokenCleanup(String token, long expirationTime) {
        long delayMs = expirationTime - System.currentTimeMillis();

        if (delayMs > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep(delayMs);
                    removeToken(token);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else {
            // Token already expired, remove immediately
            removeToken(token);
        }
    }

    /**
     * Get total number of blacklisted tokens
     */
    public int getBlacklistedTokenCount() {
        return blacklistedTokens.size();
    }
}