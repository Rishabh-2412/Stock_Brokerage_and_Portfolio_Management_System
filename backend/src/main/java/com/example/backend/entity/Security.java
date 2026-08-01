package com.example.backend.entity;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "securities", uniqueConstraints = {
    @UniqueConstraint(columnNames = "symbol")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"holdings", "transactions", "orders", "watchlist", "priceHistory"})
public class Security {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long securityId;
    
    @Column(nullable = false, unique = true)
    private String symbol;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String exchange;
    
    @Column
    private String sector;
    
    @Column(nullable = false)
    private BigDecimal currentPrice;
    
    @Column
    private BigDecimal marketCap;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    @OneToMany(mappedBy = "security", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Holdings> holdings;
    
    @OneToMany(mappedBy = "security", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
    
    @OneToMany(mappedBy = "security", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
    
    @OneToMany(mappedBy = "security", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Watchlist> watchlist;
    
    @OneToMany(mappedBy = "security", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistory> priceHistory;
    
    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}