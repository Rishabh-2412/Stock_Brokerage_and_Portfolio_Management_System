package com.example.backend.exception;

/**
 * Repurposed for this mini-project as a general "duplicate record" error
 * (e.g. username/email already exists, duplicate account number) rather
 * than a real trade-duplication detector.
 */
public class DuplicateTradeException extends RuntimeException {
    public DuplicateTradeException(String message) {
        super(message);
    }
}