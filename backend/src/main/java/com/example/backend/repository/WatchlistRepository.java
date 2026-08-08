package com.example.backend.repository;

import com.example.backend.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByAccountId(Long accountId);

    boolean existsByAccountIdAndSecurityId(Long accountId, Long securityId);
}