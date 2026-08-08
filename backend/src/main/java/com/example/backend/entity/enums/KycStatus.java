package com.example.backend.entity.enums;

/**
 * Mini-project scope: no real document verification. Every user defaults to
 * APPROVED at registration so the rest of the app (funding an account,
 * trading) is never blocked by this.
 */
public enum KycStatus {
    PENDING,
    APPROVED,
    REJECTED
}