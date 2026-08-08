package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.backend.repository")
// @EnableJpaAuditing
public class DatabaseConfig {
    // Spring Boot 4 handles DataSource, JPA, and Hibernate configuration automatically
    // via application.yml properties. No manual configuration needed.
}