package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;

/**
 * Determines how connections are handled by the {@link ThrottledHandler}.
 *
 * @see ServerThrottler
 * @see SessionThrottler
 * @since 03.03.00
 * @version 03.03.00
 * @author Ktt Development
 */
abstract class ConnectionThrottler {

    /**
     * Adds a connection to the pool.
     *
     * @param exchange exchange to process
     * @return if exchange was able to be added
     *
     * @see #deleteConnection(HttpExchange)
     * @since 03.03.00
     * @author Ktt Development
     */
    abstract boolean addConnection(final HttpExchange exchange);

    /**
     * Removes a connection from the pool.
     *
     * @param exchange exchange to process
     *
     * @see #addConnection(HttpExchange)
     * @since 03.03.00
     * @author Ktt Development
     */
    abstract void deleteConnection(final HttpExchange exchange);

}
