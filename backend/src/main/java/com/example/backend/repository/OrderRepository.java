package com.example.backend.repository;

import com.example.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountId(Long accountId);
    List<Order> findBySecurityId(Long securityId);
    List<Order> findByOrderStatus(String orderStatus);
    List<Order> findByAccountIdAndOrderStatus(Long accountId, String orderStatus);
    List<Order> findByOrderType(String orderType);
    Optional<Order> findByIdAndAccountId(Long orderId, Long accountId);
}