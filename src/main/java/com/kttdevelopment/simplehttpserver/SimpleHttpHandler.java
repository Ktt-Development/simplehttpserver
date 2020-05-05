package com.kttdevelopment.simplehttpserver;

import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * A simplified implementation of {@link HttpHandler}. It is used to process {@link SimpleHttpExchange}s. Each handler is its own thread and will not reveal any exceptions unless a try-catch within the handler is used.
 *
 * @see HttpHandler
 * @see SimpleHttpExchange
 * @since 02.00.00
 * @version 03.03.00
 * @author Ktt Development
 */

public interface SimpleHttpHandler extends HttpHandler {

    /**
     * Encapsulates the {@link #handle(SimpleHttpExchange)} for the authenticator. This method is reserved by the server; <b>do not override this</b>, it will break the {@link #handle(SimpleHttpExchange)} method.
     *
     * @param exchange client information
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    @Override
    default void handle(final HttpExchange exchange) throws IOException{
        handle(SimpleHttpExchange.create(exchange));
    }

    /**
     * Handlers the given request and generates a response <b>if no exceptions occur</b>.
     *
     * @param exchange client information
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    void handle(final SimpleHttpExchange exchange) throws IOException;

}
