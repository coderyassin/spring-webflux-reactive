package org.yascode.spring_webflux_reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yascode.spring_webflux_reactive.controller.handler.GreetingHandler;

@Configuration
public class RouterConfig {
    private final GreetingHandler greetingHandler;

    public RouterConfig(GreetingHandler greetingHandler) {
        this.greetingHandler = greetingHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions
                .route(RequestPredicates.GET("/mono"), greetingHandler::greetingGet)
                .andRoute(RequestPredicates.POST("/mono"), greetingHandler::greetingPost);
    }
}
