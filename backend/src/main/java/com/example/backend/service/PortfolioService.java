package com.example.backend.service;

import com.example.backend.dto.response.PortfolioResponse;

public interface PortfolioService {

    PortfolioResponse getPortfolio(String username, Long accountId);
}