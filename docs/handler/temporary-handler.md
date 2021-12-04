---
title: Temporary Handler
description: |
    The temporary handler is a disposable handler that removes itself after a single exchange or after a time limit.
---

# Temporary Handler

A `TemporaryHandler` limits the handler to one exchange. This can be used for single use downloads or image hosting. The handler also accepts an optional time parameter to specify the maximum time the handler can exist for.

You can use the `getRandomContext` method on the server to get a random non-conflicting context.

```java
HttpHandler handler = new HttpHandler(){

    @Override
    public void handle(HttpExchange httpExchange){
        // handle exchange
    }

};

HttpServer server = server.create(8080);

// delete handler after first exchange
server.create(server.getRandomContext(), new TemporaryHandler(handler));

// delete handler after first exchange or after 10 seconds
server.create(server.getRandomContext(), new TemporaryHandler(handler, 1000 * 10));
```