---
title: Root Handler
description: |
    A RootHandler handles requests for the root `/` context.
---

# Root Handler

By default exchanges will look for the closest matching context for their handler, this consequently means that the root index `/` would catch any requests without a handler instead of returning a code 404.

The `RootHandler` resolves this issue by only accepting requests to the exact context `/` and sending the rest to a 404 handler.

```java
HttpHandler indexHandler = new HttpHandler(){

    @Override
    public void handle(HttpExchange httpExchange){
        // handle index '/'
    }

};

HttpHandler alt404Handler = new HttpHandler(){

    @Override
    public void handle(HttpExchange httpExchange){
        // handle requests with no handler
    }

};

HttpServer server = HttpServer.create(8080);

server.create("/", new RootHandler(indexHandler, alt404Handler));
```