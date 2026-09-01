package com.example.backend.exception;

/**
 * Thrown when a user whose KYC status is not APPROVED (i.e. PENDING or
 * REJECTED) tries to perform an action that requires verification, such as
 * funding an account or placing an order.
 */
public class KycNotApprovedException extends RuntimeException {
    public KycNotApprovedException(String message) {
        super(message);
    }
}