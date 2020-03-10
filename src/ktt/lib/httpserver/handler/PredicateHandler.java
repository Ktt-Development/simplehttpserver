package ktt.lib.httpserver.handler;

import ktt.lib.httpserver.ExchangePacket;
import ktt.lib.httpserver.RequestHandler;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * The Request Handler will process each request differently depending on the predicate.
 * @see RequestHandler
 * @see Predicate
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public class PredicateHandler extends RequestHandler {

    private final RequestHandler T, F;
    private final Predicate<ExchangePacket> predicate;

    /**
     * Creates a varied Request Handler.
     * @param T handler to use if true
     * @param F handler to use if false
     * @param predicate determines which handler to use
     * @see RequestHandler
     * @since 01.00.00
     */
    public PredicateHandler(RequestHandler T, RequestHandler F, Predicate<ExchangePacket> predicate){
        this.T = T;
        this.F = F;
        this.predicate = predicate;
    }

    @Override
    public final void handle(ExchangePacket packet) throws IOException{
        if(predicate.test(packet)){
            T.handle(packet);
        }else{
            F.handle(packet);
        }
    }
}
