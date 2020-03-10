package ktt.lib.httpserver;

/**
 * Before each request is processed, the authenticator determines whether to cancel the request or now. The authenticator may also choose to process the request ahead of the handler. By default the request is untouched and returns true.
 * @since 01.00.00
 * @version 01.00.00
 * @author Ktt Development
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public interface Authenticator {

    /**
     * Handles the given request and returns true or false depending on the authentication.
     * @param packet a packet of data containing client information
     * @return if the authentication was successful
     * @see ExchangePacket
     */
    @SuppressWarnings("SameReturnValue")
    default boolean authenticate(ExchangePacket packet){ return true; }

}
