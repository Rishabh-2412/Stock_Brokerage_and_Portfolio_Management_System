package com.example.backend.config;

 
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
 
/**
 * General Application Configuration
 * Configures beans for ModelMapper, RestTemplate, Async, and Scheduling
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {
 
    /**
     * ModelMapper bean for entity-DTO mapping
     * Automatically maps entity objects to DTOs and vice versa
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Configure mapping properties if needed
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }
 
    /**
     * RestTemplate bean for making HTTP requests to external services
     * Useful for market data APIs, payment gateways, etc.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
 
    /**
     * Alternative: Create a more advanced RestTemplate with interceptors
     * Uncomment if you need custom error handling or logging
     */
    /*
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors((request, body, execution) -> {
                    request.getHeaders().set("User-Agent", "StockBrokerageApp/1.0");
                    return execution.execute(request, body);
                })
                .build();
    }
    */
}
