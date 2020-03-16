package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.server.SimpleHttpExchange;
import ktt.lib.httpserver.server.SimpleHttpHandler;

import java.io.IOException;

/**
 * A request handler that redirects to a different URL without pushing to the history.
 *
 * @see SimpleHttpHandler
 * @see com.sun.net.httpserver.HttpHandler
 * @since 01.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public class RedirectHandler extends SimpleHttpHandler {

    private final String link;

    /**
     * Creates a redirect to a URL.
     *
     * @param link URL to redirect to
     *
     * @since 01.00.00
     * @author Ktt Development
     */
    public RedirectHandler(final String link){
        this.link = link;
    }

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Location",link);
        exchange.send(HTTPCode.HTTP_Found);
    }

//


    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("RedirectHandler")   .append("{");
        OUT.append("link")              .append("= ")   .append(link);
        OUT.append("}");
        return OUT.toString();
    }

}
