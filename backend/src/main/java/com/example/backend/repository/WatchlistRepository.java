package com.example.backend.repository;

import com.example.backend.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByAccountId(Long accountId);
    Optional<Watchlist> findByAccountIdAndSecurityId(Long accountId, Long securityId);
    List<Watchlist> findBySecurityId(Long securityId);
}