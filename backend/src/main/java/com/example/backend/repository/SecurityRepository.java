package com.example.backend.repository;

import com.example.backend.entity.Security;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecurityRepository extends JpaRepository<Security, Long> {

    Optional<Security> findBySymbolIgnoreCase(String symbol);

    boolean existsBySymbolIgnoreCase(String symbol);
}