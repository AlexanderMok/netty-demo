package com.example.demo.server;

import com.example.demo.routes.RouteProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Component
public class NettyServerFactory {
    @Getter
    @Setter
    @Value("${netty.server.port:8081}")
    private int port;

    @Getter
    @Setter
    @Value("${netty.server.address:localhost}")
    private String address;

    private List<RouteProvider> routeProviders;

    public NettyServerFactory() {
    }

    public NettyServerFactory(int port) {
        this.port = port;
    }

    public NettyServer getWebServer(List<RouteProvider> routeProviders) {
        HttpServer httpServer = createHttpServer();
        ReactorHttpHandlerNettyChannelAdapter handler = new ReactorHttpHandlerNettyChannelAdapter((request, response) -> {
            log.info("Do nothing.....delegates to concrete MyNettyHttpHandler in ReactorHttpHandlerNettyChannelAdapter");
            return Mono.empty();
        });
        WrapNettyHttpServer wrapNettyHttpServer = createNettyWebServer(httpServer, handler, routeProviders);
        return wrapNettyHttpServer;
    }

    private WrapNettyHttpServer createNettyWebServer(HttpServer httpServer, ReactorHttpHandlerNettyChannelAdapter handler, List<RouteProvider> routeProviders) {
        return new WrapNettyHttpServer(httpServer, handler, routeProviders);
    }

    private HttpServer createHttpServer() {
        HttpServer server = HttpServer.create();
        server = server.bindAddress(this::getListenAddress);
        //SSL,Compression yet to configure
        server = server.protocol(HttpProtocol.HTTP11);
        return server;
    }

    private InetSocketAddress getListenAddress() {
        if (getInetAddress() != null) {
            return new InetSocketAddress(getInetAddress().getHostAddress(), getPort());
        }
        return new InetSocketAddress(getPort());
    }

    @SneakyThrows
    private InetAddress getInetAddress() {
        return InetAddress.getByName(this.address);
    }
}
