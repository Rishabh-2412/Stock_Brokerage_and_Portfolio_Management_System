package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * One row = one security saved to one account's watchlist. Unique
 * constraint prevents the same security being added twice to the same
 * account. addedAt is kept as its own field (matching the DB design doc)
 * separately from BaseEntity's inherited createdAt.
 */
@Entity
@Table(
        name = "watchlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "security_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watchlist extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    @NotNull
    private Security security;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;
}