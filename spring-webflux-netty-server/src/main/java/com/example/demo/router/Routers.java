package com.example.demo.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Functional methods to create http endpoints.
 */
@Configuration
public class Routers {

    @Bean
    public RouterFunction<ServerResponse> userGetRouter() {
        return RouterFunctions.route(
                RequestPredicates.GET("/api/v1/router/user"),
                (serverRequest) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"user\": \"you got it from functional router.\"}"));
    }
}
