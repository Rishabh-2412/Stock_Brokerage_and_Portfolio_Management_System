package com.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers a JWT "bearerAuth" scheme so Swagger UI shows an Authorize
 * button. Every @RestController should reference it via
 * @SecurityRequirement(name = "bearerAuth") once it needs auth.
 */
@Configuration
public class SwaggerConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI stockBrokerageOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Brokerage & Portfolio Management API")
                        .description("College mini-project backend")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SCHEME_NAME,
                        new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}