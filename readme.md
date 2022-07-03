# Reactor Netty Demo

This is a demo of Reactor netty server

### Case 1 - Spring Webflux

Leverage Spring Webflux to set up a reactor netty server. 

Spring provides full set of lifeCycle/filter/type conversion etc.


### Case 2 - Reactor Netty

Applied Reactor Netty API to create a netty server.
Compared with netty API, Reactor Netty provides a simpler API in a functional reactive manner.

```
NettyServerFactory -> A factory class to create NettyServer
WrapNettyHttpServer -> Wrapped Reactor Netty HttpServer to do some configuration around creation
```