package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * This handler limits the amount of active connections to a handler. This can be used to limit the amount of simultaneous downloads, or prevent duplicate connections by users.
 *
 * @see ServerThrottler
 * @see SessionThrottler
 * @since 03.03.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class ThrottledHandler implements HttpHandler {

    private final HttpHandler     handler;
    private final ConnectionThrottler throttler;

    /**
     * Creates a throttled handler using a throttler.
     *
     * @param handler handler to use
     * @param throttler how to throttle connections
     *
     * @see HttpHandler
     * @see ServerThrottler
     * @see SessionThrottler
     * @since 03.03.00
     * @author Ktt Development
     */
    public ThrottledHandler(final HttpHandler handler, final ConnectionThrottler throttler){
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

    //

    @Override
    public String toString(){
        return
            "ThrottledHandler"   + '{' +
            "handler"            + '=' +     handler     + ", " +
            "throttler"          + '=' +     throttler   +
            '}';
    }

}
