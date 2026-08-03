package com.example.backend.service.impl;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.entity.Security;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.SecurityMapper;
import com.example.backend.repository.SecurityRepository;
import com.example.backend.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private SecurityMapper securityMapper;

    @Override
    public SecurityDTO getSecurityById(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));
        return securityMapper.toDTO(security);
    }

    @Override
    public SecurityDTO getSecurityBySymbol(String symbol) {
        Security security = securityRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with symbol: " + symbol));
        return securityMapper.toDTO(security);
    }

    @Override
    public List<SecurityDTO> getAllSecurities() {
        return securityRepository.findAll().stream()
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SecurityDTO> getSecuritiesBySector(String sector) {
        return securityRepository.findBySector(sector).stream()
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SecurityDTO> getSecuritiesByExchange(String exchange) {
        return securityRepository.findByExchange(exchange).stream()
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SecurityDTO createSecurity(SecurityDTO securityDTO) {
        Security security = new Security();
        security.setSymbol(securityDTO.getSymbol());
        security.setName(securityDTO.getName());
        security.setExchange(securityDTO.getExchange());
        security.setSector(securityDTO.getSector());
        security.setCurrentPrice(securityDTO.getCurrentPrice());
        security.setMarketCap(securityDTO.getMarketCap());

        Security savedSecurity = securityRepository.save(security);
        return securityMapper.toDTO(savedSecurity);
    }

    @Override
    public SecurityDTO updateSecurity(Long securityId, SecurityDTO securityDTO) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        security.setName(securityDTO.getName());
        security.setExchange(securityDTO.getExchange());
        security.setSector(securityDTO.getSector());
        security.setCurrentPrice(securityDTO.getCurrentPrice());
        security.setMarketCap(securityDTO.getMarketCap());

        Security updatedSecurity = securityRepository.save(security);
        return securityMapper.toDTO(updatedSecurity);
    }

    @Override
    public void updatePrice(Long securityId, Double price) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));

        security.setCurrentPrice(price);
        securityRepository.save(security);
    }

    @Override
    public void deleteSecurity(Long securityId) {
        Security security = securityRepository.findById(securityId)
                .orElseThrow(() -> new ResourceNotFoundException("Security not found with id: " + securityId));
        securityRepository.delete(security);
    }

    @Override
    public List<SecurityDTO> searchSecurities(String keyword) {
        return securityRepository.findByNameContainingOrSymbolContaining(keyword, keyword).stream()
                .map(securityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean securityExists(Long securityId) {
        return securityRepository.existsById(securityId);
    }
}