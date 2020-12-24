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
 * @version 4.4.0
 * @author Ktt Development
 */
public class PredicateHandler implements HttpHandler {

    private final HttpHandler T, F;
    private final Predicate<HttpExchange> predicate;

    /**
     * Creates a predicate handler.
     *
     * @deprecated use {@link PredicateHandler#PredicateHandler(Predicate, HttpHandler, HttpHandler)}
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
    @Deprecated
    public PredicateHandler(final HttpHandler trueHandler, final HttpHandler falseHandler, final Predicate<HttpExchange> predicate){
        T = trueHandler;
        F = falseHandler;
        this.predicate = predicate;
    }

    /**
     * Creates a predicate handler.
     *
     * @param predicate predicate to test
     * @param trueHandler handler to use if true
     * @param falseHandler handler to use if false
     *
     * @see SimpleHttpHandler
     * @see HttpHandler
     * @see SimpleHttpExchange
     * @see Predicate
     * @since 4.4.0
     * @author Ktt Development
     */
    public PredicateHandler(final Predicate<HttpExchange> predicate, final HttpHandler trueHandler, final HttpHandler falseHandler){
        this.predicate = predicate;
        T = trueHandler;
        F = falseHandler;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        (predicate.test(exchange) ? T : F).handle(exchange);
    }

//

    @Override
    public String toString(){
        return
            "PredicateHandler"  + '{' +
            "(true) handler"    + '=' +     T.toString()            + ", " +
            "(false) handler"   + '=' +     F.toString()            + ", " +
            "predicate"         + '=' +     predicate.toString()    +
            '}';
    }

}
