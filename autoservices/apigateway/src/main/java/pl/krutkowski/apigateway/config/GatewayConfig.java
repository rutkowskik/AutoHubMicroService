package pl.krutkowski.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("http://localhost:8081")) // PORT AUTORYZACJI
                .route("car-service", r -> r.path("/api/v1/cars/**")
                        .uri("http://localhost:8082")) // PORT MODUŁU CAR
                .build();
    }
}
