// package com.example.backend.config;

 
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import org.springframework.orm.jpa.JpaTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;
 
// import javax.persistence.EntityManagerFactory;
 
// /**
//  * Database Configuration
//  * Configures JPA, Hibernate, and database connections
//  * Database specific settings are in application.yml
//  */
// @Configuration
// @EnableJpaRepositories(
//         basePackages = "com.examly.springapp.repository"
// )
// @EnableTransactionManagement
// @EnableJpaAuditing(
//         auditorAwareRef = "auditorAware"
// )
// public class DatabaseConfig {
 
//     /**
//      * Transaction manager bean
//      */
//     @Bean
//     public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//         return new JpaTransactionManager(entityManagerFactory);
//     }
 
//     /**
//      * Auditor aware bean for tracking created_by and last_modified_by
//      * This allows automatic population of audit fields
//      */
//     @Bean
//     public AuditorAware auditorAware() {
//         return new AuditorAwareImpl();
//     }
 
//     /**
//      * Inner class for auditing
//      * Implements Spring Data's AuditorAware interface
//      */
//     public static class AuditorAwareImpl implements org.springframework.data.domain.AuditorAware<String> {
//         @Override
//         public java.util.Optional<String> getCurrentAuditor() {
//             // In a real application, get the current user from SecurityContext
//             return java.util.Optional.of("SYSTEM");
//         }
//     }
// }