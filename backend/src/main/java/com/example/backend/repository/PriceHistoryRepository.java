package com.example.backend.repository;

import com.example.backend.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findBySecurityId(Long securityId);
    List<PriceHistory> findBySecurityIdAndDateBetween(Long securityId, LocalDate startDate, LocalDate endDate);
    List<PriceHistory> findBySecurityIdOrderByDateDesc(Long securityId);
}