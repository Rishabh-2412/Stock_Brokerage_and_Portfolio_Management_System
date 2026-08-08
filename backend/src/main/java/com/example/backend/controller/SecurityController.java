package com.example.backend.controller;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Write endpoints (create, update price) are ADMIN only. Read endpoints
 * require any authenticated user - not fully public - to keep the
 * permission model consistent with the rest of the API.
 */
@RestController
@RequestMapping("/api/securities")
@RequiredArgsConstructor
@Tag(name = "Securities", description = "Tradable instruments - list/search is open to any logged-in user, writes are ADMIN only")
@SecurityRequirement(name = "bearerAuth")
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new security (ADMIN only)")
    public ResponseEntity<SecurityDTO> createSecurity(@Valid @RequestBody SecurityDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(securityService.createSecurity(request));
    }

    @PutMapping("/{securityId}/price")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a security's current price (ADMIN only)", description = "Only currentPrice and marketCap are read from the body")
    public ResponseEntity<SecurityDTO> updatePrice(
            @PathVariable Long securityId,
            @Valid @RequestBody SecurityDTO request) {
        return ResponseEntity.ok(securityService.updatePrice(securityId, request));
    }

    @GetMapping
    @Operation(summary = "List all securities")
    public ResponseEntity<List<SecurityDTO>> getAllSecurities() {
        return ResponseEntity.ok(securityService.getAllSecurities());
    }

    @GetMapping("/{securityId}")
    @Operation(summary = "Get a security by id")
    public ResponseEntity<SecurityDTO> getById(@PathVariable Long securityId) {
        return ResponseEntity.ok(securityService.getById(securityId));
    }

    @GetMapping("/symbol/{symbol}")
    @Operation(summary = "Get a security by its ticker symbol")
    public ResponseEntity<SecurityDTO> getBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(securityService.getBySymbol(symbol));
    }
}