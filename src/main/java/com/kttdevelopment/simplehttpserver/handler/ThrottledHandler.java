package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ThrottledHandler implements HttpHandler {

    private final HttpHandler     handler;
    private final ServerThrottler throttler;

    public ThrottledHandler(final HttpHandler handler, final ServerThrottler throttler){
        this.handler = handler;
        this.throttler = throttler;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        if(throttler.addConnection(exchange)){
            handler.handle(exchange);
            throttler.deleteConnection(exchange);
        }else{
            exchange.close();
        }
    }

}
