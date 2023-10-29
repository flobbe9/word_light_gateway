package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Config for routing of all APIs.
 * 
 * @since 0.0.1
 */
@Configuration
public class RouterConfig {

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                        .route("backend", route -> 
                            route.path("/api/documentBuilder/**")
                                 .uri("http://localhost:4001"))

                        .route("user_service", route -> 
                            route.path("/api/appUser/**")
                                 .uri("http://localhost:4002"))
                        .build();
    }
}