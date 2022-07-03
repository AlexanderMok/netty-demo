package com.example.demo.config;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class HeaderWriterWebFilter implements WebFilter {

    private final HeaderWriter headerWriters;

    public HeaderWriterWebFilter(HeaderWriter headerWriter) {
        this.headerWriters = headerWriter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        this.headerWriters.writerSecurityHeaders(response);
        return chain.filter(exchange);
    }
}
