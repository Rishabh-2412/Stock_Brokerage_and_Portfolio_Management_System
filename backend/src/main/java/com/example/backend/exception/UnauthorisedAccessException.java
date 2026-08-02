package com.example.backend.exception;

public class UnauthorisedAccessException extends RuntimeException {
    public UnauthorisedAccessException(String message) {
        super(message);
    }

    public UnauthorisedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}