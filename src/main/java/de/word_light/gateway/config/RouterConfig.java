package de.word_light.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;


/**
 * Config for routing of all APIs.
 * 
 * @since 0.0.1
 */
@Configuration
@EnableWebFlux
// TODO: update docker-compose.all.yml 
// TODO: try with ssl
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

    @Value("${BASE_URL}")
    private String BASE_URL;


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
                                // assuming response to gateway, since everything is routed by it
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, BASE_URL)
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                            .uri(DOCUMENT_BUILDER_BASE_URL))

                        .route("user_service", route -> route
                            .path("/" + USER_SERVICE_MAPPING + "/**")
                            // remove duplicate headers, order of calls matters!
                            .filters(filter -> filter
                                // assuming response to gateway, since everything is routed by it
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, BASE_URL)
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                                .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                                .removeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                            .uri(USER_SERVICE_BASE_URL))

                        .route("frontend", route -> route
                            .path("/**")
                            .uri(FRONTEND_BASE_URL))
                        .build();
    }
}