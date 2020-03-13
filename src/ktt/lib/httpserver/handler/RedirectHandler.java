package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.server.SimpleHttpExchange;
import ktt.lib.httpserver.server.SimpleHttpHandler;

import java.io.IOException;

public class RedirectHandler extends SimpleHttpHandler {

    private final String link;

    public RedirectHandler(final String link){
        this.link = link;
    }

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Location",link);
        exchange.send(HTTPCode.HTTP_Found);
    }

}
