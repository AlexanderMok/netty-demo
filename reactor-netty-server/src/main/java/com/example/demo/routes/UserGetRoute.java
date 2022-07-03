package com.example.demo.routes;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRoutes;

@Component
public class UserGetRoute implements RouteProvider {

    private Routes routes;

    public UserGetRoute(Routes routes) {
        this.routes = routes;
    }

    @Override
    public HttpServerRoutes apply(HttpServerRoutes httpServerRoutes) {
        return httpServerRoutes.get(
                routes.getUSER_GET_ROUTE(),
                ((httpServerRequest, httpServerResponse) ->
                        httpServerResponse.status(HttpResponseStatus.OK)
                                .header(HttpHeaderNames.CONTENT_TYPE, "application/json")
                                .sendString(Mono.just("{\"user\": \"you got it from functional router.\"}"))
                )
        );
    }
}
