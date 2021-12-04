---
title: Creating A Server
description: |
    Quick start guide for creating a SimpleHttpServer.
---

# Creating A Simple Http Server

A `SimpleHttpServer` is created the same way that a native [http server](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html) is created; using the `create` method.

```java
SimpleHttpServer server = SimpleHttpServer.create();
// https
SimpleHttpsServer hserver = SimpleHttpsServer.create();
```

# Binding To A Port

For a server to start it must be binded to a port. The port can be either an integer of an [`InetSocketAddress`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/InetSocketAddress.html).

```java
SimpleHttpServer server = SimpleHttpServer.create(8080);
```

```java
SimpleHttpServer server = SimpleHttpServer.create(new InetSocketAddress(8080));
```

If a server is not binded an instantiation then it must be binded using the `bind` method.

```java
SimpleHttpServer server = SimpleHttpServer.create();
server.bind(8080);
```

```java
SimpleHttpServer server = SimpleHttpServer.create();
server.bind(new InetSocketAddress(8080));
```

A server can only be binded to a port once, it can not be changed after it is set.

If a port is already occoupied by another process then a [`BindException`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/BindException.html) will be thrown.

# Starting The Server

The server can be started by using the `start` method on the server. A server can only be started if it is binded to a port.

```java
SimpleHttpServer server = SimpleHttpServer.create(8080);
server.start();
```

# Stopping The Server

The server can be stopped by using the `stop` method. This will stop all inbound requests and currently active exchanges. An optional integer parameter can be supplied to specify how long to wait for current exchanges to complete before closing the server.

```java
SimpleHttpServer server = SimpleHttpServer.create(8080);
server.start();
// stop all exchanges
server.stop();
```

```java
SimpleHttpServer server = SimpleHttpServer.create(8080);
server.start();
// stop current exchanges after 10 seconds
server.stop(10);
```