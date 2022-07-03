package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class DemoSpringWebFluxApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testController(){
        webClient.get()
                .uri("/api/v1/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json("{\"user\": \"you got it from controller.\"}");
    }

    @Test
    public void testControllerWithCors(){
        webClient.get()
                .uri("http://localhost:8080/api/v1/user")
                .header(HttpHeaders.ORIGIN,"http://localhost:8081")
                .exchange()
                .expectHeader().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                .expectStatus().is2xxSuccessful()
                .expectBody().json("{\"user\": \"you got it from controller.\"}");
    }

    @Test
    public void testRouter() {
        webClient.get()
                .uri("/api/v1/router/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json("{\"user\": \"you got it from functional router.\"}");
    }

    @Test
    public void testRouterWithCors() {
        webClient.get()
                .uri("http://localhost:8080/api/v1/router/user")
                .header(HttpHeaders.ORIGIN,"http://localhost:8081")
                .exchange()
                .expectHeader().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                .expectStatus().is2xxSuccessful();
    }
}