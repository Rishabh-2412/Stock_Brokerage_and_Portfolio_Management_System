package com.example.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    private static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
    private static final String IFSC_REGEX = "^[A-Z]{4}0[A-Z0-9]{6}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern IFSC_PATTERN = Pattern.compile(IFSC_REGEX);

    /**
     * Validate email format
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Email validation failed: email is null or empty");
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate username format
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Username validation failed: username is null or empty");
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validate phone number format
     */
    public boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            logger.warn("Phone validation failed: phone is null or empty");
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate full name
     */
    public boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            logger.warn("Full name validation failed: name is null or empty");
            return false;
        }
        
        String name = fullName.trim();
        if (name.length() < 3 || name.length() > 100) {
            logger.warn("Full name validation failed: length requirement not met");
            return false;
        }

        // Allow only letters, spaces, and hyphens
        return name.matches("^[a-zA-Z\\s'-]+$");
    }

    /**
     * Validate IFSC code (Indian bank code)
     */
    public boolean isValidIFSC(String ifsc) {
        if (ifsc == null || ifsc.trim().isEmpty()) {
            logger.warn("IFSC validation failed: IFSC is null or empty");
            return false;
        }
        return IFSC_PATTERN.matcher(ifsc).matches();
    }

    /**
     * Validate order quantity
     */
    public boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    /**
     * Validate price
     */
    public boolean isValidPrice(double price) {
        return price > 0 && price < Double.MAX_VALUE;
    }

    /**
     * Validate account balance
     */
    public boolean isValidBalance(double balance) {
        return balance >= 0 && balance < Double.MAX_VALUE;
    }

    /**
     * Validate transaction amount
     */
    public boolean isValidTransactionAmount(double amount) {
        return amount > 0 && amount < Double.MAX_VALUE;
    }

    /**
     * Validate order type
     */
    public boolean isValidOrderType(String orderType) {
        if (orderType == null || orderType.trim().isEmpty()) {
            return false;
        }
        return orderType.equalsIgnoreCase("buy") || orderType.equalsIgnoreCase("sell");
    }

    /**
     * Validate order status
     */
    public boolean isValidOrderStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        return status.equalsIgnoreCase("pending") || 
               status.equalsIgnoreCase("market") || 
               status.equalsIgnoreCase("limit") || 
               status.equalsIgnoreCase("stop");
    }

    /**
     * Validate transaction type
     */
    public boolean isValidTransactionType(String transactionType) {
        if (transactionType == null || transactionType.trim().isEmpty()) {
            return false;
        }
        return transactionType.equalsIgnoreCase("buy") || 
               transactionType.equalsIgnoreCase("sell") || 
               transactionType.equalsIgnoreCase("dividend");
    }

    /**
     * Validate account type
     */
    public boolean isValidAccountType(String accountType) {
        if (accountType == null || accountType.trim().isEmpty()) {
            return false;
        }
        return accountType.equalsIgnoreCase("cash") || 
               accountType.equalsIgnoreCase("margin") || 
               accountType.equalsIgnoreCase("demo");
    }

    /**
     * Validate user account type
     */
    public boolean isValidUserAccountType(String userAccountType) {
        if (userAccountType == null || userAccountType.trim().isEmpty()) {
            return false;
        }
        return userAccountType.equalsIgnoreCase("individual") || 
               userAccountType.equalsIgnoreCase("institutional");
    }

    /**
     * Validate KYC status
     */
    public boolean isValidKycStatus(String kycStatus) {
        if (kycStatus == null || kycStatus.trim().isEmpty()) {
            return false;
        }
        return kycStatus.equalsIgnoreCase("pending") || 
               kycStatus.equalsIgnoreCase("approved") || 
               kycStatus.equalsIgnoreCase("rejected");
    }

    /**
     * Validate security symbol
     */
    public boolean isValidSecuritySymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            return false;
        }
        // Allow alphanumeric and hyphen, 1-10 chars
        return symbol.matches("^[A-Z0-9-]{1,10}$");
    }

    /**
     * Check if string is null or empty
     */
    public boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validate margin requirement percentage
     */
    public boolean isValidMarginPercentage(double marginPercentage) {
        return marginPercentage > 0 && marginPercentage <= 100;
    }

    /**
     * Validate commission percentage
     */
    public boolean isValidCommissionPercentage(double commission) {
        return commission >= 0 && commission <= 100;
    }

    /**
     * Get validation error message
     */
    public String getValidationError(String fieldName, String fieldValue) {
        switch (fieldName.toLowerCase()) {
            case "email":
                if (isNullOrEmpty(fieldValue)) return "Email cannot be empty";
                if (!isValidEmail(fieldValue)) return "Invalid email format";
                break;
            case "username":
                if (isNullOrEmpty(fieldValue)) return "Username cannot be empty";
                if (!isValidUsername(fieldValue)) 
                    return "Username must be 3-20 characters, alphanumeric and underscore only";
                break;
            case "phone":
                if (isNullOrEmpty(fieldValue)) return "Phone cannot be empty";
                if (!isValidPhone(fieldValue)) return "Invalid phone format";
                break;
            case "fullname":
                if (isNullOrEmpty(fieldValue)) return "Full name cannot be empty";
                if (!isValidFullName(fieldValue)) return "Invalid full name format";
                break;
        }
        return null;
    }
}