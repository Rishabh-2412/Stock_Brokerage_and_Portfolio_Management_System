package com.example.backend.repository;

import com.example.backend.entity.Holdings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingsRepository extends JpaRepository<Holdings, Long> {
    List<Holdings> findByAccountId(Long accountId);
    Optional<Holdings> findByAccountIdAndSecurityId(Long accountId, Long securityId);
    List<Holdings> findBySecurityId(Long securityId);
}