package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.*;
import com.sun.net.httpserver.HttpExchange;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Limits connections per http session.
 *
 * @see ThrottledHandler
 * @see ServerThrottler
 * @see HttpSession
 * @since 03.03.00
 * @version 03.03.00
 * @author Ktt Development
 */
public class SessionThrottler extends ConnectionThrottler {

    private final HttpSessionHandler sessionHandler;

    private final Predicate<HttpSession> countsTowardsLimit;
    private final Map<HttpSession, AtomicInteger> sessions = Collections.synchronizedMap(new HashMap<>());

    private final AtomicInteger maxConnections = new AtomicInteger(0);

    public SessionThrottler(final HttpSessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
        countsTowardsLimit = (exchange) -> true;
    }

    public SessionThrottler(final HttpSessionHandler sessionHandler, final int maxConnections){
        this.sessionHandler = sessionHandler;
        countsTowardsLimit = (exchange) -> true;
        this.maxConnections.set(maxConnections);
    }

    public SessionThrottler(final HttpSessionHandler sessionHandler, final Predicate<HttpSession> countsTowardsLimit){
        this.sessionHandler = sessionHandler;
        this.countsTowardsLimit = countsTowardsLimit;
    }

    public SessionThrottler(final HttpSessionHandler sessionHandler, final Predicate<HttpSession> countsTowardsLimit, final int maxConnections){
        this.sessionHandler = sessionHandler;
        this.countsTowardsLimit = countsTowardsLimit;
        this.maxConnections.set(maxConnections);
    }

    public final int getMaxConnections(){
        return maxConnections.get();
    }

    public synchronized final void setMaxConnections(final int maxConnections){
        if(maxConnections >= 0)
            this.maxConnections.set(maxConnections);
    }

    @Override
    final boolean addConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        if(!countsTowardsLimit.test(session)){
            return true;
        }else{
            synchronized(this){
                final AtomicInteger conn = sessions.get(session);
                if(conn.get() + 1 <= maxConnections.get()){
                    conn.incrementAndGet();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    final void deleteConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        if(countsTowardsLimit.test(session))
            synchronized(this){
                sessions.get(session).decrementAndGet();
            }
    }

    //

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();

        OUT.append("SessionThrottler")  .append('{');
        OUT.append("condition")         .append('=')    .append(countsTowardsLimit)     .append(", ");
        OUT.append("sessions")          .append('=')    .append(sessions.toString())    .append(", ");
        OUT.append("maxConnections")    .append('=')    .append(maxConnections.get());
        OUT.append('}');

        return OUT.toString();
    }

}
