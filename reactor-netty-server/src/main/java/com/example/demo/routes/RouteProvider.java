package com.example.demo.routes;

import reactor.netty.http.server.HttpServerRoutes;

import java.util.function.Function;

public interface RouteProvider extends Function<HttpServerRoutes, HttpServerRoutes> {
}
