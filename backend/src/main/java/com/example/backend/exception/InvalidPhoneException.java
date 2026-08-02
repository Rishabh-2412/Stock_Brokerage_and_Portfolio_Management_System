package com.example.backend.exception;

public class InvalidPhoneException extends RuntimeException {
    public InvalidPhoneException(String message) {
        super(message);
    }

    public InvalidPhoneException(String message, Throwable cause) {
        super(message, cause);
    }
}