package com.example.backend.service.validator;

import com.example.backend.entity.User;
import com.example.backend.exception.InvalidNameException;
import com.example.backend.exception.InvalidPhoneException;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String PHONE_REGEX = "^[0-9]{10,15}$";
    private static final String NAME_REGEX = "^[a-zA-Z\\s]{2,50}$";

    public void validateUserRegistration(User user) {
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validatePassword(user.getPasswordHash());
        validateFullName(user.getFullName());
    }

    public void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidNameException("Username cannot be empty");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new InvalidNameException("Username must be between 3 and 20 characters");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidNameException("Email cannot be empty");
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new InvalidNameException("Invalid email format");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidNameException("Password cannot be empty");
        }
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new InvalidNameException("Password must be at least 8 characters with uppercase, lowercase, digit and special character");
        }
    }

    public void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidNameException("Full name cannot be empty");
        }
        if (!Pattern.matches(NAME_REGEX, fullName)) {
            throw new InvalidNameException("Full name must contain only letters and spaces (2-50 characters)");
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new InvalidPhoneException("Phone number cannot be empty");
        }
        if (!Pattern.matches(PHONE_REGEX, phoneNumber)) {
            throw new InvalidPhoneException("Phone number must be 10-15 digits");
        }
    }

    public void validateAccountType(String accountType) {
        if (!accountType.equals("individual") && !accountType.equals("institutional")) {
            throw new InvalidNameException("Invalid account type");
        }
    }

    public void validateKycStatus(String kycStatus) {
        if (!kycStatus.equals("pending") && !kycStatus.equals("approved") && 
            !kycStatus.equals("rejected")) {
            throw new InvalidNameException("Invalid KYC status");
        }
    }
}