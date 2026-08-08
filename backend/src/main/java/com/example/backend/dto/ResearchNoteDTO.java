package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * On CREATE (POST /api/research-notes): only title, content, and
 * (optionally) securityId are read. authorId/authorName are set from the
 * logged-in user automatically - not taken from the request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "A research note - fields used depend on the endpoint, see per-field notes")
public class ResearchNoteDTO {

    @Schema(example = "9", description = "Response only - ignored on create")
    private Long noteId;

    @Schema(example = "4", description = "Response only - taken from the logged-in RESEARCH_ANALYST")
    private Long authorId;

    @Schema(example = "priya_analyst", description = "Response only")
    private String authorName;

    @Schema(example = "3", description = "Optional on create - omit for general market commentary")
    private Long securityId;

    @Schema(example = "TCS", description = "Response only")
    private String symbol;

    @NotBlank(message = "title is required")
    @Schema(example = "TCS Q1 earnings beat expectations")
    private String title;

    @NotBlank(message = "content is required")
    @Schema(example = "Revenue grew 12% YoY, margins expanded 80bps. Maintaining BUY rating with target of 4200.")
    private String content;

    @Schema(example = "2026-08-07T11:00:00", description = "Response only")
    private LocalDateTime createdAt;
}