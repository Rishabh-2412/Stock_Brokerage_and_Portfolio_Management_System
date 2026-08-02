package com.example.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;

    /**
     * Encode password using BCrypt
     */
    public String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            logger.warn("Attempt to encode null or empty password");
            return null;
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verify if raw password matches encoded password
     */
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            logger.warn("Null password provided for matching");
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Validate password strength
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            logger.warn("Password validation failed: password is null or empty");
            return false;
        }

        // Check length
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            logger.warn("Password validation failed: length requirement not met");
            return false;
        }

        // Check for uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            logger.warn("Password validation failed: no uppercase letter");
            return false;
        }

        // Check for lowercase letter
        if (!password.matches(".*[a-z].*")) {
            logger.warn("Password validation failed: no lowercase letter");
            return false;
        }

        // Check for digit
        if (!password.matches(".*\\d.*")) {
            logger.warn("Password validation failed: no digit");
            return false;
        }

        // Check for special character
        if (!password.matches(".*[@$!%*?&].*")) {
            logger.warn("Password validation failed: no special character");
            return false;
        }

        return true;
    }

    /**
     * Get password validation error message
     */
    public String getPasswordValidationError(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long";
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            return "Password must not exceed " + MAX_PASSWORD_LENGTH + " characters";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }

        if (!password.matches(".*[@$!%*?&].*")) {
            return "Password must contain at least one special character (@$!%*?&)";
        }

        return null;
    }

    /**
     * Check if password needs to be changed (optional - for security policies)
     */
    public boolean isPasswordExpired(Long lastPasswordChangeTime, long expirationDays) {
        if (lastPasswordChangeTime == null) {
            return true;
        }
        long currentTime = System.currentTimeMillis();
        long expirationTime = expirationDays * 24 * 60 * 60 * 1000L;
        return (currentTime - lastPasswordChangeTime) > expirationTime;
    }

    /**
     * Generate a temporary password
     */
    public String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&";
        StringBuilder tempPassword = new StringBuilder();
        int length = 12;
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            tempPassword.append(chars.charAt(index));
        }
        return tempPassword.toString();
    }
}