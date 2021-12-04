---
title: Predicate Handler
description: |
    A predicate handler executes one of two handlers depending on a predicate condition.
---

# Predicate Handler

The `PredicateHandler` accepts a [`Predicate`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Predicate.html)[`<HttpExchange>`](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpExchange.html) and uses that to determine which handler to send the request to.

```java
Predicate<HttpExchange> predicate = new Predicate<>(){

    @Override
    public boolean test(HttpExchange exchange){
        return true;
    }

};

HttpHandler trueHandler = new HttpHandler(){

    @Override
    public void handle(HttpExchange httpExchange){
        // handle if true
    }

};

HttpHandler falseHandler = new HttpHandler(){

    @Override
    public void handle(HttpExchange httpExchange){
        // handle if false
    }

};

HttpServer server = HttpServer.create(8080);

server.createContext("/", new PredicateHandler(predicate, trueHandler, falseHandler));
```