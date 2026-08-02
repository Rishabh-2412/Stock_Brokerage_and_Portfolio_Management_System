package com.example.backend.exception;

public class InsufficientMarginException extends RuntimeException {
    public InsufficientMarginException(String message) {
        super(message);
    }

    public InsufficientMarginException(String message, Throwable cause) {
        super(message, cause);
    }
}