package com.example.demo.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryConfiguration}
 * {@link org.springframework.boot.web.embedded.netty.NettyWebServer}
 * <p>
 * NettyWebServer is managed by {@link org.springframework.boot.web.reactive.context.WebServerManager}
 * and {@link org.springframework.boot.web.reactive.context.WebServerStartStopLifecycle}.
 * <p>
 * {@link NettyReactiveWebServerFactory} will create netty http server while WebServerManager is initializing.
 * <p>
 * WebServerStartStopLifecycle is started by {@link org.springframework.context.support.DefaultLifecycleProcessor}.
 *
 * Some Beans are in {@link ReactiveWebServerFactoryAutoConfiguration},{@link NettyAutoConfiguration}, {@link HttpHandlerAutoConfiguration}.
 * Defining some beans here just to clearly show reactor-netty server's start.
 * Set the following to override beans in AutoConfiguration beans.
 * <code>
 *     spring:
 *       main:
 *         allow-bean-definition-overriding: true
 * </code>
 *
 */
@Configuration
public class NettyServerConfig {

    private final ApplicationContext applicationContext;

    NettyServerConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ReactiveWebServerFactoryCustomizer reactiveWebServerFactoryCustomizer(ServerProperties serverProperties) {
        return new ReactiveWebServerFactoryCustomizer(serverProperties);
    }

    @Bean
    public ReactorResourceFactory reactorServerResourceFactory() {
        return new ReactorResourceFactory();
    }

    @Bean
    public NettyReactiveWebServerFactory nettyReactiveWebServerFactory(ReactorResourceFactory resourceFactory,
                                                                ObjectProvider<NettyRouteProvider> routes,
                                                                ObjectProvider<NettyServerCustomizer> serverCustomizers) {
        NettyReactiveWebServerFactory serverFactory = new NettyReactiveWebServerFactory();
        serverFactory.setResourceFactory(resourceFactory);
        routes.orderedStream().forEach(serverFactory::addRouteProviders);
        serverFactory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
        return serverFactory;
    }

    /**
     * WebHttpHandlerBuilder decorates/assembles httpHandler with Routers/WebFilter/WebExceptionHandlers/.. etc.
     * @param propsProvider
     * @return
     */
    @Bean
    public HttpHandler httpHandler(ObjectProvider<WebFluxProperties> propsProvider) {
        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(this.applicationContext).build();
        WebFluxProperties properties = propsProvider.getIfAvailable();
        if (properties != null && StringUtils.hasText(properties.getBasePath())) {
            Map<String, HttpHandler> handlersMap = Collections.singletonMap(properties.getBasePath(), httpHandler);
            return new ContextPathCompositeHandler(handlersMap);
        }
        return httpHandler;
    }
}