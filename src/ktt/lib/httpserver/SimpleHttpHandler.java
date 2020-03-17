package ktt.lib.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * A simplified implementation of {@link HttpHandler}. It is used to process {@link SimpleHttpExchange}s.
 *
 * @see HttpHandler
 * @see SimpleHttpExchange
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public abstract class SimpleHttpHandler implements HttpHandler, SimpleHttpExchangeAuthenticator {

    /**
     * Encapsulates the {@link #handle(SimpleHttpExchange)} for the authenticator. Applications do not use this.
     *
     * @param exchange client information
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        final SimpleHttpExchange sxe = SimpleHttpExchange.create(exchange);
        if(authenticate(sxe))
            handle(sxe);
        else
            sxe.close();
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
    public abstract void handle(final SimpleHttpExchange exchange) throws IOException;

}
