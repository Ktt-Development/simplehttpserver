package ktt.lib.httpserver.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class TemporaryHandler implements HttpHandler, SimpleHttpExchangeAuthenticator {

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        final SimpleHttpExchange sxe = SimpleHttpExchange.create(exchange);
        if(authenticate(sxe)){
            handle(sxe);
            sxe.getHttpServer().removeContext(sxe.getHttpContext());
        }else{
            sxe.close();
        }
    }

    public abstract void handle(final SimpleHttpExchange exchange) throws IOException;

}
