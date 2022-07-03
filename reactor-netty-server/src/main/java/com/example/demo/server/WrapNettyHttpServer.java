package com.example.demo.server;

import com.example.demo.routes.RouteProvider;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.unix.Errors;
import io.netty.util.concurrent.DefaultEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.web.server.PortInUseException;
import org.springframework.boot.web.server.WebServerException;
import reactor.netty.ChannelBindException;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Slf4j
public class WrapNettyHttpServer implements NettyServer {

    private static final int ERROR_NO_EACCES = -13;

    /**
     * Primitive Reactor Netty Server instance.
     */
    private HttpServer httpServer;

    private List<RouteProvider> routeProviders = Collections.emptyList();

    private final BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> handler;

    private volatile DisposableServer disposableServer;


    public WrapNettyHttpServer(HttpServer httpServer, ReactorHttpHandlerNettyChannelAdapter handler, List<RouteProvider> routeProviders) {
        this.httpServer = httpServer;
        this.handler = handler;
        this.httpServer = httpServer.channelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
        this.routeProviders = routeProviders;
    }

    @Override
    public void start() {
        if (this.disposableServer == null) {
            try {
                this.disposableServer = startHttpServer();
            } catch (Exception ex) {
                PortInUseException.ifCausedBy(ex, ChannelBindException.class, (bindException) -> {
                    if (bindException.localPort() > 0 && !isPermissionDenied(bindException.getCause())) {
                        throw new PortInUseException(bindException.localPort(), ex);
                    }
                });
                throw new WebServerException("Unable to start Netty", ex);
            }
            if (this.disposableServer != null) {
                log.info("Netty started" + getStartedOnMessage(this.disposableServer));
            }
            startDaemonAwaitThread(this.disposableServer);
        }
    }

    private DisposableServer startHttpServer() {
        HttpServer server = this.httpServer;

        if (this.routeProviders.isEmpty()) {
            server = server.handle(this.handler);
        } else {
            server = server.route(this::applyRouteProviders);
        }

        server = server.handle(this.handler);
        return server.bindNow();
    }

    private void startDaemonAwaitThread(DisposableServer disposableServer) {
        Thread awaitThread = new Thread("server") {

            @Override
            public void run() {
                disposableServer.onDispose().block();
            }

        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void stop() {
        if (this.disposableServer != null) {
            try {
                log.info("Disposing netty server resources.");
                this.disposableServer.disposeNow();
            } catch (IllegalStateException ex) {
                // Continue
            }
            this.disposableServer = null;
            log.info("Netty server resources disposed.");
        }

    }

    private void applyRouteProviders(HttpServerRoutes routes) {
        for (RouteProvider provider : this.routeProviders) {
            routes = provider.apply(routes);
        }
        routes.route((request) -> true, this.handler);
    }

    private boolean isPermissionDenied(Throwable bindExceptionCause) {
        try {
            if (bindExceptionCause instanceof Errors.NativeIoException) {
                return ((Errors.NativeIoException) bindExceptionCause).expectedErr() == ERROR_NO_EACCES;
            }
        } catch (Throwable ex) {
        }
        return false;
    }

    private String getStartedOnMessage(DisposableServer server) {
        StringBuilder message = new StringBuilder();
        tryAppend(message, "port %s", server::port);
        tryAppend(message, "path %s", server::path);
        return (message.length() > 0) ? " on " + message : "";
    }

    private void tryAppend(StringBuilder message, String format, Supplier<Object> supplier) {
        try {
            Object value = supplier.get();
            message.append((message.length() != 0) ? " " : "");
            message.append(String.format(format, value));
        } catch (UnsupportedOperationException ex) {
        }
    }
}
