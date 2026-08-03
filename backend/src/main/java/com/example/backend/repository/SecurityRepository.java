package com.example.backend.repository;

import com.example.backend.entity.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {
    Optional<Security> findBySymbol(String symbol);
    List<Security> findByExchange(String exchange);
    List<Security> findBySector(String sector);
    boolean existsBySymbol(String symbol);
}