package com.example.backend.config;

 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
 
/**
 * JWT Configuration class to hold JWT-related properties
 * These values are typically loaded from application.yml
 */
@Configuration
public class JwtConfig {
 
    @Value("${jwt.secret:your-secret-key-change-this-in-production}")
    private String secret;
 
    @Value("${jwt.expiration:86400000}")  // Default: 24 hours in milliseconds
    private long expiration;
 
    @Value("${jwt.refresh.expiration:604800000}")  // Default: 7 days in milliseconds
    private long refreshExpiration;
 
    @Value("${jwt.header:Authorization}")
    private String header;
 
    @Value("${jwt.prefix:Bearer }")
    private String prefix;
 
    // Getters
    public String getSecret() {
        return secret;
    }
 
    public long getExpiration() {
        return expiration;
    }
 
    public long getRefreshExpiration() {
        return refreshExpiration;
    }
 
    public String getHeader() {
        return header;
    }
 
    public String getPrefix() {
        return prefix;
    }
}
