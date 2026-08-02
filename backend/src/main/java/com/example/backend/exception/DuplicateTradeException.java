package com.example.backend.exception;

public class DuplicateTradeException extends RuntimeException {
    public DuplicateTradeException(String message) {
        super(message);
    }

    public DuplicateTradeException(String message, Throwable cause) {
        super(message, cause);
    }
}