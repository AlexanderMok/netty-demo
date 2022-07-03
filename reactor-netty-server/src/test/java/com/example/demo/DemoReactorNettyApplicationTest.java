package com.example.demo;


import com.example.demo.routes.UserGetRoute;
import com.example.demo.server.NettyServer;
import com.example.demo.server.NettyServerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;

class DemoReactorNettyApplicationTest {

    private AnnotationConfigApplicationContext applicationContext;
    private NettyServer nettyServer;
    private HttpClient httpClient;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.scan("com.example.demo");
        applicationContext.refresh();
        startServer();
        httpClient = HttpClient.create();
    }

    private void startServer() {
        NettyServerFactory factory = applicationContext.getBean(NettyServerFactory.class);
        UserGetRoute userRoute = applicationContext.getBean(UserGetRoute.class);
        nettyServer = factory.getWebServer(Collections.singletonList(userRoute));
        nettyServer.start();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        nettyServer.stop();
        applicationContext.close();
    }

    @Test
    public void testUserGetRoute() {
        httpClient.get()
                .uri("http://localhost:8081/api/v2/router/user")
                .responseContent()
                .aggregate()
                .asString()
                .doOnSuccess(System.out::println)
                .block();
    }
}