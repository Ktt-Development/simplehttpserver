package ktt.lib.httpserver.handler;

import com.sun.net.httpserver.HttpHandler;
import ktt.lib.httpserver.SimpleHttpExchange;
import ktt.lib.httpserver.SimpleHttpHandler;

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
public class PredicateHandler extends SimpleHttpHandler {

    private final HttpHandler T, F;
    private final Predicate<SimpleHttpExchange> predicate;

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
    public PredicateHandler(final HttpHandler trueHandler, final HttpHandler falseHandler, final Predicate<SimpleHttpExchange> predicate){
        T = trueHandler;
        F = falseHandler;
        this.predicate = predicate;
    }

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        (predicate.test(exchange) ? T : F).handle(exchange.getHttpExchange());
    }

//


    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();
        OUT.append("PredicateHandler")  .append("{");
        OUT.append("(true) handler")    .append("=")   .append(T.toString())           .append(", ");
        OUT.append("(false) handler")   .append("=")   .append(F.toString())           .append(", ");
        OUT.append("predicate")         .append("=")   .append(predicate.toString());
        OUT.append("}");
        return OUT.toString();
    }

}
