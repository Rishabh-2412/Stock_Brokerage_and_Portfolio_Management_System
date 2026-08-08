package com.example.backend.mapper;

import com.example.backend.dto.SecurityDTO;
import com.example.backend.entity.Security;

public class SecurityMapper {

    private SecurityMapper() {
    }

    public static SecurityDTO toDTO(Security security) {
        if (security == null) {
            return null;
        }
        return SecurityDTO.builder()
                .securityId(security.getId())
                .symbol(security.getSymbol())
                .name(security.getName())
                .exchange(security.getExchange())
                .sector(security.getSector())
                .currentPrice(security.getCurrentPrice())
                .marketCap(security.getMarketCap())
                .lastUpdated(security.getLastUpdated())
                .build();
    }
}