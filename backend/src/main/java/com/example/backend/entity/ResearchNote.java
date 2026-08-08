package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple research note posted by a RESEARCH_ANALYST. Not part of the
 * original DB design doc - added purely to give the RESEARCH_ANALYST role
 * something real to do (per the SRS role list), kept intentionally thin:
 * no workflow, no approval process, no versioning. Only references User
 * and Security as the non-owning side (@ManyToOne) - does not require any
 * change to either of those entities.
 */
@Entity
@Table(name = "research_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull
    private User author;

    /** Optional - a note can be general market commentary with no specific security. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id")
    private Security security;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}