package ktt.lib.httpserver.server;

public interface SimpleHttpExchangeAuthenticator {

    default boolean authenticate(final SimpleHttpExchange exchange){ return true; }

}
