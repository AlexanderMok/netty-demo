package com.example.demo.server;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public interface MyNettyHttpHandler {
    Mono<Void> handle(HttpServerRequest request, HttpServerResponse response);
}
