package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TemporaryHandler implements HttpHandler {

    private final HttpHandler handler;

    private boolean hasExpiry;
    private long initTime;
    private long maxTime;

    public TemporaryHandler(final HttpHandler handler){
        this.handler = handler;
    }

    public TemporaryHandler(final HttpHandler handler, final long maxTime){
        this.handler = handler;

        hasExpiry = true;
        initTime = System.currentTimeMillis();
        this.maxTime = maxTime;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        if(!hasExpiry || maxTime + initTime < System.currentTimeMillis())
            handler.handle(exchange);
        exchange.getHttpContext().getServer().removeContext(exchange.getHttpContext());
    }

}
