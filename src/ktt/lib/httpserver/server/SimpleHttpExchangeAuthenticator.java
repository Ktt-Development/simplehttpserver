package ktt.lib.httpserver.server;

import ktt.lib.httpserver.server.SimpleHttpExchange;

public interface SimpleHttpExchangeAuthenticator {

    default boolean authenticate(final SimpleHttpExchange exchange){ return true; }

}
