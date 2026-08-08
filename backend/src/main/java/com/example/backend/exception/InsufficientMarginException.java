package com.example.backend.exception;

/**
 * Repurposed for this mini-project as a simple "insufficient funds" error
 * (e.g. cash_available < order value) rather than a real margin-call engine.
 */
public class InsufficientMarginException extends RuntimeException {
    public InsufficientMarginException(String message) {
        super(message);
    }
}