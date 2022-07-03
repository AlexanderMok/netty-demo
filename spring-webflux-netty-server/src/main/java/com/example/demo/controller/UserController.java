package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<String> userGet() {
        return Mono.just("{\"user\": \"you got it from controller.\"}");
    }
}
