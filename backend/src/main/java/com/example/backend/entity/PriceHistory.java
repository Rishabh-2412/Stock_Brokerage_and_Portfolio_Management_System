package com.example.backend.entity;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
 
@Entity
@Table(name = "price_history", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"security_id", "date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"security"})
public class PriceHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    private Security security;
    
    @Column(nullable = false)
    private BigDecimal openPrice;
    
    @Column(nullable = false)
    private BigDecimal highPrice;
    
    @Column(nullable = false)
    private BigDecimal lowPrice;
    
    @Column(nullable = false)
    private BigDecimal closePrice;
    
    @Column(nullable = false)
    private Long volume;
    
    @Column(nullable = false)
    private LocalDate date;
}
