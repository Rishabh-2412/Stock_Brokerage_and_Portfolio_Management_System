package com.example.backend.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 
import java.util.Arrays;
import java.util.Collections;
 
/**
 * CORS Configuration
 * Configures Cross-Origin Resource Sharing for API endpoints
 */
@Configuration
public class CorsConfig {
 
    /**
     * Configure CORS settings for all API endpoints
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
 
        // Allow requests from these origins
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",           // React dev server
                "http://localhost:8080",           // Angular dev server
                "http://localhost:4200",           // Another Angular port
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8080",
                "https://yourdomain.com",          // Production domain
                "https://www.yourdomain.com"
        ));
 
        // Allow these HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH"
        ));
 
        // Allow these headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Cache-Control",
                "X-CSRF-Token",
                "X-API-Key"
        ));
 
        // Expose these headers to the client
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "X-Total-Count",
                "X-Page-Number",
                "X-Page-Size"
        ));
 
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
 
        // Max age for preflight requests (in seconds) - 1 hour
        configuration.setMaxAge(3600L);
 
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
 
        return source;
    }
}
