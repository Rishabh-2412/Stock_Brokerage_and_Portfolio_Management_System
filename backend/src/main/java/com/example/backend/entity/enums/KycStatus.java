package com.example.backend.entity.enums;

/**
 * Self-registered CLIENT users now start at PENDING and must be approved by
 * an ADMIN (via PATCH /api/users/{userId}/kyc-status) before they can fund
 * an account or place orders. Users created directly by an ADMIN (via
 * POST /api/users) are auto-APPROVED, since an admin is already vouching
 * for them.
 */
public enum KycStatus {
    PENDING,
    APPROVED,
    REJECTED
}