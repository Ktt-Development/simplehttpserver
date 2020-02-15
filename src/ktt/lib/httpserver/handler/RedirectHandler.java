package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.ExchangePacket;
import ktt.lib.httpserver.RequestHandler;
import ktt.lib.httpserver.http.HTTPCode;

import java.io.IOException;

/**
 * A Request Handler that redirects to a different URL without saving to the history.
 * @see RequestHandler
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings("unused")
public final class RedirectHandler extends RequestHandler {

    private final String link;

    /**
     * Creates a redirect to a URL.
     * @param link URL to redirect to
     * @since 01.00.00
     */
    public RedirectHandler(String link){
        this.link = link;
    }

    @Override
    public void handle(ExchangePacket packet) throws IOException{
        packet.getResponseHeaders().set("Location",link);
        packet.send(HTTPCode.HTTP_Found);
    }
}
