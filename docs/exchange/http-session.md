---
title: Http Session
description: An Http Session keeps track of a user across multiple exchanges.
---

# Http Session

Normally the only "id" type data that we can retrieve from the user is their address and port provided from the exchange. This however, doesn't work across multiple tabs or when the user refreshes the page; instead we use a session cookie to track a user.

Every time an exchange happens a user is assigned an http session cookie. Each session has an id, creation time, and last accessed time.

For sessions to work the server must have a `HttpSessionHandler` set.

```java
SimpleHttpServer server = new SimpleHttpServer(8080);
// required
server.setHttpSessionHandler(new HttpSessionHandler());

HttpHandler handler = new HttpHandler(){

    @Override
    public void handle(HttpExchange exchange){
        HttpSession session = server.getHttpSession(exchange);
        exchange.send(200);
        exchange.close();
    }

};
```

# Http Session Handler

The session handler keeps track of all active sessions and determines the name of the session cookie; by default this is `__session-id`.

The session cookie and ID assignment can be overridden in the `assignSessionId` method.

```java
HttpSessionHandler sessions = new HttpSessionHandler("_session-cookie"){

    @Override
    public String assignSessionId(HttpExchange exchange){
        return UUID.randomUUID().toString();
    }

};

SimpleHttpServer server = SimpleHttpServer.create(8080);
server.setHttpSessionHandler(sessions);
```