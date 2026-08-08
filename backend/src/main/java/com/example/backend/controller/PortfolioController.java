package com.example.backend.controller;

import com.example.backend.dto.response.PortfolioResponse;
import com.example.backend.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Tag(name = "Portfolio", description = "Read-only, live-computed holdings view with unrealized P&L")
@SecurityRequirement(name = "bearerAuth")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get an account's live portfolio (owner, or ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER)",
            description = "Computed fresh on every call from Holdings + each Security's current price - nothing is cached")
    public ResponseEntity<PortfolioResponse> getPortfolio(
            Authentication authentication,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(portfolioService.getPortfolio(authentication.getName(), accountId));
    }
}