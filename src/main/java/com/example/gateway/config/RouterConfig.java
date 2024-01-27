package com.example.gateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;


/**
 * Config for routing of all APIs.
 * 
 * @since 0.0.1
 */
@Configuration
@EnableWebFlux
public class RouterConfig implements WebFluxConfigurer {
    
    @Value("${DOCUMENT_BUILDER_BASE_URL}")
    private String documentBuilderBaseUrl;
    
    @Value("${USER_SERVICE_BASE_URL}")
    private String userServiceBaseUrl;

    @Value("${FRONTEND_BASE_URL}")
    private String frontendBaseUrl;


    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                        .route("document_builder", route -> 
                            route.path("/api/documentBuilder/**")
                                .filters(filter -> filter
                                    // remove duplicate headers, order of calls matters!
                                    .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, frontendBaseUrl)
                                    .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                                    .addResponseHeader("Access-Control-Allow-Credentials", "true")
                                    .removeResponseHeader("Access-Control-Allow-Credentials")
                                )
                                 .uri(documentBuilderBaseUrl))

                        .route("user_service", route -> 
                            route.path("/api/appUser/**")
                                 .uri(userServiceBaseUrl))
                        .build();
    }


    /**
     * Configure CORS to allow frontend url and set some more headers.
     * 
     * @return configured {@link CorsWebFilter}
     */
    @Bean
    CorsWebFilter corsWebFilter() {
        
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of(frontendBaseUrl));
        corsConfig.addAllowedMethod(HttpMethod.GET.name());
        corsConfig.addAllowedMethod(HttpMethod.POST.name());
        corsConfig.addAllowedMethod(HttpMethod.PUT.name());
        corsConfig.addAllowedMethod(HttpMethod.DELETE.name());
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS.name());
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}