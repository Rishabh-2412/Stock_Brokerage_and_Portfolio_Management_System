package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}

// Component	Strategy
// Entities - Create all 8 entities together (minimal code per entity)
// DTOs	- Group by feature (Auth DTOs, Order DTOs, etc.)
// Repositories	- Auto-generated via Spring Data JPA interfaces
// Services	- Create per feature (AuthService, OrderService, etc.)
// Controllers	- Create per feature with all endpoints
// Security	- All JWT/Auth code in one batch
// Config	- All configuration files together