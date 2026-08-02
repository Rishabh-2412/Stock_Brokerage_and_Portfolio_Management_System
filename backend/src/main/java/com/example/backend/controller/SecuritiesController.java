package com.example.backend.controller;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/securities")
@CrossOrigin(origins = "*")
public class SecuritiesController {

    @Autowired
    private SecurityService securityService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SecurityDTO> addSecurity(@Valid @RequestBody SecurityDTO securityDTO) {
        SecurityDTO createdSecurity = securityService.addSecurity(securityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSecurity);
    }

    @GetMapping("/{securityId}")
    public ResponseEntity<SecurityDTO> getSecurityById(@PathVariable Long securityId) {
        SecurityDTO security = securityService.getSecurityById(securityId);
        return ResponseEntity.ok(security);
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<SecurityDTO> getSecurityBySymbol(@PathVariable String symbol) {
        SecurityDTO security = securityService.getSecurityBySymbol(symbol);
        return ResponseEntity.ok(security);
    }

    @GetMapping
    public ResponseEntity<List<SecurityDTO>> getAllSecurities() {
        List<SecurityDTO> securities = securityService.getAllSecurities();
        return ResponseEntity.ok(securities);
    }

    @GetMapping("/exchange/{exchange}")
    public ResponseEntity<List<SecurityDTO>> getSecuritiesByExchange(@PathVariable String exchange) {
        List<SecurityDTO> securities = securityService.getSecuritiesByExchange(exchange);
        return ResponseEntity.ok(securities);
    }

    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<SecurityDTO>> getSecuritiesBySector(@PathVariable String sector) {
        List<SecurityDTO> securities = securityService.getSecuritiesBySector(sector);
        return ResponseEntity.ok(securities);
    }

    @PutMapping("/{securityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SecurityDTO> updateSecurity(@PathVariable Long securityId,
            @Valid @RequestBody SecurityDTO securityDTO) {
        SecurityDTO updatedSecurity = securityService.updateSecurity(securityId, securityDTO);
        return ResponseEntity.ok(updatedSecurity);
    }

    @PutMapping("/{securityId}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SecurityDTO> updateSecurityPrice(@PathVariable Long securityId, @RequestParam Double price) {
        SecurityDTO security = securityService.updateSecurityPrice(securityId, price);
        return ResponseEntity.ok(security);
    }

    @DeleteMapping("/{securityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSecurity(@PathVariable Long securityId) {
        securityService.deleteSecurity(securityId);
        return ResponseEntity.ok("Security deleted successfully");
    }

    @GetMapping("/top-gainers")
    public ResponseEntity<List<SecurityDTO>> getTopGainers(@RequestParam(defaultValue = "10") int limit) {
        List<SecurityDTO> gainers = securityService.getTopGainers(limit);
        return ResponseEntity.ok(gainers);
    }

    @GetMapping("/top-losers")
    public ResponseEntity<List<SecurityDTO>> getTopLosers(@RequestParam(defaultValue = "10") int limit) {
        List<SecurityDTO> losers = securityService.getTopLosers(limit);
        return ResponseEntity.ok(losers);
    }

    @GetMapping("/most-active")
    public ResponseEntity<List<SecurityDTO>> getMostActive(@RequestParam(defaultValue = "10") int limit) {
        List<SecurityDTO> active = securityService.getMostActive(limit);
        return ResponseEntity.ok(active);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SecurityDTO>> searchSecurities(@RequestParam String query) {
        List<SecurityDTO> securities = securityService.searchSecurities(query);
        return ResponseEntity.ok(securities);
    }
}
