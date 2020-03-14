package ktt.lib.httpserver.server;

/**
 * Before each request is processed, the authenticator determines whether to cancel the request or not. The authenticator may also choose to process the request ahead of the handler. By default it returns true.
 *
 * @see SimpleHttpExchange
 * @since 01.00.00
 * @version 02.00.00
 */
public interface SimpleHttpExchangeAuthenticator {

    /**
     * Handles the given request and returns if the exchange was authenticated.
     *
     * @param exchange exchange from client
     * @return if authentication was successful
     *
     * @see SimpleHttpExchange
     * @since 01.00.00
     * @author Ktt Development
     */
    default boolean authenticate(final SimpleHttpExchange exchange){ return true; }

}
