package com.example.backend.service.impl;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.entity.Security;
import com.example.backend.exception.DuplicateTradeException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.SecurityMapper;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityServiceImpl implements SecurityService {

    private final SecurityRepository securityRepository;

    @Override
    public SecurityDTO createSecurity(SecurityDTO request) {
        String symbol = request.getSymbol().toUpperCase();

        if (securityRepository.existsBySymbolIgnoreCase(symbol)) {
            throw new DuplicateTradeException("Security already exists for symbol: " + symbol);
        }

        Security security = Security.builder()
                .symbol(symbol)
                .name(request.getName())
                .exchange(request.getExchange())
                .sector(request.getSector())
                .currentPrice(request.getCurrentPrice())
                .marketCap(request.getMarketCap())
                .lastUpdated(LocalDateTime.now())
                .build();

        return SecurityMapper.toDTO(securityRepository.save(security));
    }

    @Override
    public SecurityDTO updatePrice(Long securityId, SecurityDTO request) {
        Security security = getSecurityOrThrow(securityId);

        security.setCurrentPrice(request.getCurrentPrice());
        if (request.getMarketCap() != null) {
            security.setMarketCap(request.getMarketCap());
        }
        security.setLastUpdated(LocalDateTime.now());

        return SecurityMapper.toDTO(securityRepository.save(security));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityDTO> getAllSecurities() {
        return securityRepository.findAll()
                .stream()
                .map(SecurityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SecurityDTO getById(Long securityId) {
        return SecurityMapper.toDTO(getSecurityOrThrow(securityId));
    }

    @Override
    @Transactional(readOnly = true)
    public SecurityDTO getBySymbol(String symbol) {
        Security security = securityRepository.findBySymbolIgnoreCase(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found for symbol: " + symbol));
        return SecurityMapper.toDTO(security);
    }

    private Security getSecurityOrThrow(Long securityId) {
        return securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found: " + securityId));
    }
}