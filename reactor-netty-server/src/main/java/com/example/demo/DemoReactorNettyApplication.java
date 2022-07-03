package com.example.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DemoReactorNettyApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.refresh();
        Runtime.getRuntime().addShutdownHook(new Thread(applicationContext::close));
    }
}
