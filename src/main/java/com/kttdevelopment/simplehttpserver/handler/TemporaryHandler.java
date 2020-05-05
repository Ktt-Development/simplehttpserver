package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TemporaryHandler implements HttpHandler {

    private final HttpHandler handler;

    public TemporaryHandler(final HttpHandler handler){
        this.handler = handler;
    }

    public TemporaryHandler(final HttpHandler handler, final long maxTime){
        this.handler = handler;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        handler.handle(exchange);
        exchange.getHttpContext().getServer().removeContext(exchange.getHttpContext());
    }

}
