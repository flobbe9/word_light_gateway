package de.word_light.gateway.config;

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
    private String DOCUMENT_BUILDER_BASE_URL;
    @Value("${DOCUMENT_BUILDER_MAPPING}")
    private String DOCUMENT_BUILDER_MAPPING;

    @Value("${USER_SERVICE_BASE_URL}")
    private String USER_SERVICE_BASE_URL;
    @Value("${USER_SERVICE_MAPPING}")
    private String USER_SERVICE_MAPPING;

    @Value("${FRONTEND_BASE_URL}")
    private String FRONTEND_BASE_URL;


    /**
     * Handles routing of word light.<p>
     * 
     * @param builder
     * @return
     * @since 0.0.1
     */
    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                        .route("document_builder", route -> route
                            .path("/" + DOCUMENT_BUILDER_MAPPING + "/**")
                            // remove duplicate headers, order of calls matters!
                            .filters(filter -> filter
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONTEND_BASE_URL)
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*"))
                            .uri(DOCUMENT_BUILDER_BASE_URL))

                        .route("user_service", route -> route
                            .path("/" + USER_SERVICE_MAPPING + "/**")
                            // remove duplicate headers, order of calls matters!
                            .filters(filter -> filter
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONTEND_BASE_URL)
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                            .uri(USER_SERVICE_BASE_URL))
                        .build();
    }


    /**
     * Configure CORS.
     * 
     * @return
     */
    @Bean
    CorsWebFilter corsWebFilter() {
        
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of(FRONTEND_BASE_URL));
        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.POST);
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}