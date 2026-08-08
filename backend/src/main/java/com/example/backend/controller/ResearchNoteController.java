package com.example.backend.controller;

import com.example.backend.dto.ResearchNoteDTO;
import com.example.backend.service.ResearchNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Write is RESEARCH_ANALYST/ADMIN only. Read is open to any logged-in user
 * (a CLIENT should be able to read research before trading) - consistent
 * with how Securities read access works.
 */
@RestController
@RequestMapping("/api/research-notes")
@RequiredArgsConstructor
@Tag(name = "Research Notes", description = "Research analyst commentary - write is RESEARCH_ANALYST/ADMIN only, read is open to any logged-in user")
@SecurityRequirement(name = "bearerAuth")
public class ResearchNoteController {

    private final ResearchNoteService researchNoteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('RESEARCH_ANALYST','ADMIN')")
    @Operation(summary = "Post a new research note (RESEARCH_ANALYST/ADMIN only)")
    public ResponseEntity<ResearchNoteDTO> createNote(
            Authentication authentication,
            @Valid @RequestBody ResearchNoteDTO request) {
        ResearchNoteDTO response = researchNoteService.createNote(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all research notes, most recent first")
    public ResponseEntity<List<ResearchNoteDTO>> getAllNotes() {
        return ResponseEntity.ok(researchNoteService.getAllNotes());
    }

    @GetMapping("/security/{securityId}")
    @Operation(summary = "List research notes for one security, most recent first")
    public ResponseEntity<List<ResearchNoteDTO>> getNotesForSecurity(@PathVariable Long securityId) {
        return ResponseEntity.ok(researchNoteService.getNotesForSecurity(securityId));
    }
}