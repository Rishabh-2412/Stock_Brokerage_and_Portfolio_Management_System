package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Get user by ID with all details
     * @param userId user identifier
     * @return UserDTO with complete user information
     */
    UserDTO getUserById(Long userId);

    /**
     * Get user by email address
     * @param email user email
     * @return Optional containing UserDTO if exists
     */
    Optional<UserDTO> getUserByEmail(String email);

    /**
     * Get user by username
     * @param username user login name
     * @return Optional containing UserDTO if exists
     */
    Optional<UserDTO> getUserByUsername(String username);

    /**
     * Get all users with pagination
     * @param pageNumber page index
     * @param pageSize number of records per page
     * @return List of UserDTOs
     */
    List<UserDTO> getAllUsers(int pageNumber, int pageSize);

    /**
     * Update user profile information
     * @param userId user identifier
     * @param userDTO updated user data
     * @return updated UserDTO
     */
    UserDTO updateUserProfile(Long userId, UserDTO userDTO);

    /**
     * Update user KYC status
     * @param userId user identifier
     * @param kycStatus new KYC status (pending/approved/rejected)
     */
    void updateKycStatus(Long userId, String kycStatus);

    /**
     * Get user KYC status
     * @param userId user identifier
     * @return KYC status string
     */
    String getKycStatus(Long userId);

    /**
     * Verify KYC documents
     * @param userId user identifier
     * @param panNumber PAN card number
     * @param dematAccount DEMAT account number
     * @return true if verification successful
     */
    boolean verifyKyc(Long userId, String panNumber, String dematAccount);

    /**
     * Check if email exists and is available
     * @param email email address to check
     * @return true if email already exists
     */
    boolean emailExists(String email);

    /**
     * Check if username exists and is available
     * @param username username to check
     * @return true if username already exists
     */
    boolean usernameExists(String username);

    /**
     * Delete user account
     * @param userId user identifier
     */
    void deleteUser(Long userId);

    /**
     * Change user password with validation
     * @param userId user identifier
     * @param oldPassword current password
     * @param newPassword new password (8+ chars, uppercase, lowercase, digit, special char)
     * @return true if password changed successfully
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * Get user account type
     * @param userId user identifier
     * @return account type (individual/institutional)
     */
    String getAccountType(Long userId);

    /**
     * Lock user account after failed login attempts
     * @param userId user identifier
     */
    void lockAccount(Long userId);

    /**
     * Unlock user account
     * @param userId user identifier
     */
    void unlockAccount(Long userId);

    /**
     * Check if user account is locked
     * @param userId user identifier
     * @return true if account is locked
     */
    boolean isAccountLocked(Long userId);
}