package com.example.backend.entity.enums;

/**
 * Maps to Users.account_type in the DB design doc (individual/institutional).
 * Named UserCategory here (not AccountType) to avoid clashing with
 * entity.enums.AccountType, which describes a *trading* Account's type
 * (CASH/MARGIN/DEMO) from the Accounts module.
 */
public enum UserCategory {
    INDIVIDUAL,
    INSTITUTIONAL
}