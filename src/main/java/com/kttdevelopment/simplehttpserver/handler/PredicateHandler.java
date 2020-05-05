package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.kttdevelopment.simplehttpserver.SimpleHttpExchange;
import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * The handler will process each request differently depending on the predicate.
 *
 * @see SimpleHttpHandler
 * @see HttpHandler
 * @see SimpleHttpExchange
 * @see Predicate
 * @since 01.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
public class PredicateHandler implements HttpHandler {

    private final HttpHandler T, F;
    private final Predicate<HttpExchange> predicate;

    /**
     * Creates a predicate handler.
     *
     * @param trueHandler handler to use if true
     * @param falseHandler handler to use if false
     * @param predicate predicate to test
     *
     * @see SimpleHttpHandler
     * @see HttpHandler
     * @see SimpleHttpExchange
     * @see Predicate
     * @since 01.00.00
     * @author Ktt Development
     */
    public PredicateHandler(final HttpHandler trueHandler, final HttpHandler falseHandler, final Predicate<HttpExchange> predicate){
        T = trueHandler;
        F = falseHandler;
        this.predicate = predicate;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        (predicate.test(exchange) ? T : F).handle(exchange);
    }

//


    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("PredicateHandler")  .append('{');
        OUT.append("(true) handler")    .append('=')   .append(T.toString())           .append(", ");
        OUT.append("(false) handler")   .append('=')   .append(F.toString())           .append(", ");
        OUT.append("predicate")         .append('=')   .append(predicate.toString());
        OUT.append('}');
        return OUT.toString();
    }

}
