package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;

/**
 *
 */
abstract class ConnectionThrottler {

    abstract boolean addConnection(final HttpExchange exchange);

    abstract void deleteConnection(final HttpExchange exchange);

}
