package com.example.backend.repository;

import com.example.backend.entity.Holdings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingsRepository extends JpaRepository<Holdings, Long> {

    Optional<Holdings> findByAccountIdAndSecurityId(Long accountId, Long securityId);

    List<Holdings> findByAccountId(Long accountId);
}