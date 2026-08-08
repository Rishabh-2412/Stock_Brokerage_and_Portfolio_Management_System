package com.example.backend.service;

import com.example.backend.dto.SecurityDTO;

import java.util.List;

public interface SecurityService {

    SecurityDTO createSecurity(SecurityDTO request);

    SecurityDTO updatePrice(Long securityId, SecurityDTO request);

    List<SecurityDTO> getAllSecurities();

    SecurityDTO getById(Long securityId);

    SecurityDTO getBySymbol(String symbol);
}