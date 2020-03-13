package ktt.lib.httpserver.handler;

import com.sun.net.httpserver.HttpHandler;
import ktt.lib.httpserver.server.SimpleHttpExchange;
import ktt.lib.httpserver.server.SimpleHttpHandler;

import java.io.IOException;
import java.util.function.Predicate;

public class PredicateHandler extends SimpleHttpHandler {

    private final HttpHandler T, F;
    private final Predicate<SimpleHttpExchange> predicate;

    public PredicateHandler(final HttpHandler trueHandler, final HttpHandler falseHandler, final Predicate<SimpleHttpExchange> predicate){
        T = trueHandler;
        F = falseHandler;
        this.predicate = predicate;
    }

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        if(predicate.test(exchange)){
            T.handle(exchange.getHttpExchange());
        }else{
            F.handle(exchange.getHttpExchange());
        }
    }

}
