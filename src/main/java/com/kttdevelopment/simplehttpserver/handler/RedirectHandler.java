package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.var.HttpCode;
import com.kttdevelopment.simplehttpserver.SimpleHttpExchange;
import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;

import java.io.IOException;

/**
 * A request handler that redirects to a different URL without pushing to the history. The URL may not work correctly if it does not have a valid authority (<code>http</code>/<code>https</code>).
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
        try{
            System.out.println("started");
            exchange.getResponseHeaders().set("Location", link);
            exchange.send(HttpCode.HTTP_Found);
        }catch(final Exception e){
            e.printStackTrace();
        }
    }

//


    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("RedirectHandler")   .append("{");
        OUT.append("link")              .append("=")   .append(link);
        OUT.append("}");
        return OUT.toString();
    }

}
