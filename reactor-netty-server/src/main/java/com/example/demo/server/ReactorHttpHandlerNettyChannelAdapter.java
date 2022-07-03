package com.example.demo.server;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.function.BiFunction;

/**
 * Represents handler function in Netty Channel
 */
@Slf4j
public class ReactorHttpHandlerNettyChannelAdapter implements BiFunction<HttpServerRequest, HttpServerResponse, Mono<Void>> {

    private MyNettyHttpHandler httpHandler;

    public ReactorHttpHandlerNettyChannelAdapter(MyNettyHttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    @Override
    public Mono<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        log.debug("Received request {}", request);
        log.debug("Can do some pre condition check on request or do something else.");
        try {
            return this.httpHandler.handle(request, response)
                    .doOnError(ex -> log.error("Failed to complete: " + ex.getMessage()))
                    .doOnSuccess(aVoid -> log.info("Handling completed"));
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to get request URI: " + ex.getMessage());
            }
            response.status(HttpResponseStatus.BAD_REQUEST);
            return Mono.empty();
        }
    }

}
