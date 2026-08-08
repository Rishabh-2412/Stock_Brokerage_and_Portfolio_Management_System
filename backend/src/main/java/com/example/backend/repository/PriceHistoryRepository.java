package com.example.backend.repository;

import com.example.backend.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findBySecurityIdOrderByDateAsc(Long securityId);

    List<PriceHistory> findBySecurityIdAndDateBetweenOrderByDateAsc(Long securityId, LocalDate start, LocalDate end);

    Optional<PriceHistory> findBySecurityIdAndDate(Long securityId, LocalDate date);
}