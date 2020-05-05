package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * A temporary handler handles a single request and then removes itself from the server. This can be used for single use downloads, or media file hosting.
 *
 * @see HttpHandler
 * @since 03.03.00
 * @version 03.03.00
 * @author Ktt Development
 */
public class TemporaryHandler implements HttpHandler {

    private final HttpHandler handler;

    private boolean hasExpiry;
    private long initTime;
    private long maxTime;
    private long expiry;

    /**
     * Creates a temporary handler that removes itself after the first connection.
     *
     * @param handler handler to use
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public TemporaryHandler(final HttpHandler handler){
        this.handler = handler;
    }

    /**
     * Creates a temporary handler that removes itself after the first connection, or after the time expires.
     *
     * @param handler handler to use
     * @param maxTime how long the handler may exists for in milliseconds
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public TemporaryHandler(final HttpHandler handler, final long maxTime){
        this.handler = handler;

        hasExpiry = true;
        initTime = System.currentTimeMillis();
        this.maxTime = maxTime;
        expiry = initTime + maxTime;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        if(!hasExpiry || expiry < System.currentTimeMillis())
            handler.handle(exchange);
        exchange.getHttpContext().getServer().removeContext(exchange.getHttpContext());
    }

    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("TemporaryHandler")  .append('{');
        OUT.append("handler")           .append('=')    .append(handler.toString()) .append(", ");
        if(hasExpiry){
            OUT.append("initTime")      .append('=')    .append(initTime)           .append(", ");
            OUT.append("maxTime")       .append('=')    .append(maxTime)            .append(", ");
            OUT.append("expiry")        .append('=')    .append(expiry);
        }
        OUT.append('}');
       return OUT.toString();
    }

}
