package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;

/**
 * Determines how connections are handled by the {@link ThrottledHandler}.
 *
 * @see ThrottledHandler
 * @see ExchangeThrottler
 * @see ServerExchangeThrottler
 * @see SessionThrottler
 * @see ServerSessionThrottler
 * @since 03.03.00
 * @version 03.05.00
 * @author Ktt Development
 */
abstract class ConnectionThrottler {

    /**
     * Adds a connection to the pool.
     *
     * @param exchange exchange to process
     * @return if exchange was able to be added
     *
     * @see HttpExchange
     * @see #deleteConnection(HttpExchange)
     * @see #getMaxConnections(HttpExchange)
     * @since 03.03.00
     * @author Ktt Development
     */
    abstract boolean addConnection(final HttpExchange exchange);

    /**
     * Removes a connection from the pool.
     *
     * @param exchange exchange to process
     *
     * @see HttpExchange
     * @see #addConnection(HttpExchange)
     * @see #getMaxConnections(HttpExchange)
     * @since 03.03.00
     * @author Ktt Development
     */
    abstract void deleteConnection(final HttpExchange exchange);

    /**
     * Returns the maximum number of connections for an exchange. A value of <code>-1</code> means unlimited connections.
     *
     * @param exchange exchange to process
     * @return maximum number of connections allowed
     *
     * @see HttpExchange
     * @see #addConnection(HttpExchange)
     * @see #deleteConnection(HttpExchange)
     * @since 03.05.00
     * @author Ktt Development
     */
    public abstract int getMaxConnections(final HttpExchange exchange);

}
