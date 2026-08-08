package com.example.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Binds to application.yml under `app.jwt.*`. See the application.yml
 * snippet provided alongside this module.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    /** Must be at least 32 characters (256 bits) for HS256. */
    private String secret;

    /** Token validity in milliseconds. Default 24h if not set in yml. */
    private long expirationMs = 86_400_000L;
}