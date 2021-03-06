package com.example.demo;

import com.example.demo.routes.UserGetRoute;
import com.example.demo.server.NettyServer;
import com.example.demo.server.NettyServerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;

@Slf4j
public class DemoReactorNettyApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.scan("com.example.demo");
        applicationContext.refresh();
        NettyServerFactory factory = applicationContext.getBean(NettyServerFactory.class);
        UserGetRoute userRoute = applicationContext.getBean(UserGetRoute.class);
        NettyServer nettyServer = factory.getWebServer(Collections.singletonList(userRoute));
        nettyServer.start();

        HttpClient client = HttpClient.create();
        client.get()
                .uri("http://localhost:8081/api/v2/router/user")
                .responseContent()
                .aggregate()
                .asString()
                .doOnSuccess(System.out::println)
                .block();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("In shutdown hook.");
            nettyServer.stop();
            applicationContext.close();
        }));
    }
}
