package com.example.backend.service;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Register a new user with validation
     * @param registerRequest contains username, email, password, full_name
     * @return AuthResponse with JWT token and user details
     */
    AuthResponse register(RegisterRequest registerRequest);

    /**
     * Login user with credentials validation
     * @param loginRequest contains email/username and password
     * @return AuthResponse with JWT token on success
     */
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Logout user and blacklist token
     * @param token JWT token to invalidate
     */
    void logout(String token);

    /**
     * Validate JWT token
     * @param token JWT token to validate
     * @return true if valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Refresh JWT token with 30-day rotation
     * @param refreshToken existing refresh token
     * @return new AuthResponse with updated tokens
     */
    AuthResponse refreshToken(String refreshToken);

    /**
     * Extract userId from JWT token
     * @param token JWT token
     * @return userId extracted from token claims
     */
    Long extractUserIdFromToken(String token);

    /**
     * Extract email from JWT token
     * @param token JWT token
     * @return email extracted from token claims
     */
    String extractEmailFromToken(String token);

    /**
     * Initiate password reset with OTP
     * @param email user email address
     */
    void initiatePasswordReset(String email);

    /**
     * Reset password with OTP verification
     * @param email user email
     * @param otp one-time password sent to email
     * @param newPassword new password meeting security requirements
     */
    void resetPassword(String email, String otp, String newPassword);
}