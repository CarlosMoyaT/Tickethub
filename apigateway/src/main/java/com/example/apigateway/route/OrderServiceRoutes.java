package com.example.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class OrderServiceRoutes {

    @Bean
    public RouterFunction<ServerResponse> orderRoutes() {
        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("/api/v1/orders/**"),
                        request -> HandlerFunctions.http("http://localhost:8082").handle(request))
                .build();
    }
}
