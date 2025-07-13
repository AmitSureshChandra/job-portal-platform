package com.jobportal.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                
                // Company Service Routes
                .route("company-service", r -> r.path("/api/companies/**")
                        .uri("lb://company-service"))
                
                // Job Service Routes
                .route("job-service", r -> r.path("/api/jobs/**")
                        .uri("lb://job-service"))
                
                // Application Service Routes
                .route("application-service", r -> r.path("/api/applications/**")
                        .uri("lb://application-service"))
                
                // Search Service Routes
                .route("search-service", r -> r.path("/api/search/**")
                        .uri("lb://search-service"))
                
                // Notification Service Routes
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .uri("lb://notification-service"))
                
                .build();
    }
}