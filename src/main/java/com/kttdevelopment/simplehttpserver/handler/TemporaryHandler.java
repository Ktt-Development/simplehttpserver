package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * A temporary handler handles a single request and then removes itself from the server. This can be used for single use downloads, or media file hosting.
 * For time based expiry the handler will only remove itself if a user connects to the handler after the time expires.
 *
 * @see HttpHandler
 * @see SimpleHttpServer#getRandomContext()
 * @see SimpleHttpServer#getRandomContext(String)
 * @since 03.03.00
 * @version 03.05.02
 * @author Ktt Development
 */
public class TemporaryHandler implements HttpHandler {

    private SimpleHttpServer server = null;
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
     * @see HttpHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    public TemporaryHandler(final HttpHandler handler){
        this.handler = handler;
    }

    /**
     * Creates a temporary handler that removes itself after the first connection.
     *
     * @param server simple http server; required if you want {@link SimpleHttpServer#getContexts()} to work properly
     * @param handler handler to use
     *
     * @see SimpleHttpServer
     * @see HttpHandler
     * @since 03.05.02
     */
    public TemporaryHandler(final SimpleHttpServer server, final HttpHandler handler){
        this.server = server;
        this.handler = handler;
    }

    /**
     * Creates a temporary handler that removes itself after the first connection, or after the time expires.
     *
     * @param handler handler to use
     * @param maxTime how long the handler may exists for in milliseconds
     *
     * @see HttpHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    public TemporaryHandler(final HttpHandler handler, final long maxTime){
        this(null,handler,maxTime);
    }

    /**
     * Creates a temporary handler that removes itself after the first connection, or after the time expires.
     *
     * @param server simple http server; required if you want {@link SimpleHttpServer#getContexts()} to work properly
     * @param handler handler to use
     * @param maxTime how long the handler may exists for in milliseconds
     *
     * @see SimpleHttpServer
     * @see HttpHandler
     * @since 03.05.02
     * @author Ktt Development
     */
    public TemporaryHandler(final SimpleHttpServer server, final HttpHandler handler, final long maxTime){
        this.server = server;
        this.handler = handler;

        hasExpiry = true;
        initTime = System.currentTimeMillis();
        this.maxTime = maxTime;
        expiry = initTime + maxTime;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        if(!hasExpiry || System.currentTimeMillis() < expiry)
            handler.handle(exchange);
        if(server == null)
            exchange.getHttpContext().getServer().removeContext(exchange.getHttpContext());
        else
            server.removeContext(exchange.getHttpContext());
        exchange.close();
    }

    @Override
    public String toString(){
        return
            "TemporaryHandler"  + '{' +
            "handler"           + '=' +     handler.toString() +
            (hasExpiry
             ?  ", " +
                "initTime"      + '=' +     initTime    + ", " +
                "maxTime"       + '=' +     maxTime     + ", " +
                "expiry"        + '=' +     expiry
             : ""
            ) +
            '}';
    }

}
