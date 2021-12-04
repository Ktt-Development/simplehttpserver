---
title: SSE Handler
description: |
    The server sent events (SSE) handler allows the server to communicate with the client without requiring a new exchange.
---

# Server Sent Events (SSE) Handler

The server sent events `SSEHandler` allows a constant server to client stream of data. More information on SSE can be found [here](https://www.w3schools.com/html/html5_serversentevents.asp).

# Sending Events

Events can be sent using the `push` method in the SSE handler.

```java
SSEHandler handler = new SSEHandler();

HttpServer server = HttpServer.create(8080);
// create an event stream at localhost:8080/event
server.createContext("event", handler);
server.start();

new Thread(){

    @Override
    public void run(){
        // send the current time to the user each second
        while(true){
            handler.push(String.valueOf(System.currentTimeMillis()));
            Thread.sleep(1000);
        }
    }

}.start();
```