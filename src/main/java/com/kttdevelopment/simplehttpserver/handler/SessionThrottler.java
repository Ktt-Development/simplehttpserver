package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.*;
import com.sun.net.httpserver.HttpExchange;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Limits connections per http session. This can be used to limit simultaneous downloads.
 *
 * @see ThrottledHandler
 * @see ServerThrottler
 * @see HttpSession
 * @since 03.03.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class SessionThrottler extends ConnectionThrottler {

    private final HttpSessionHandler sessionHandler;

    private final Predicate<HttpSession> countsTowardsLimit;
    private final Map<HttpSession, AtomicInteger> sessions = Collections.synchronizedMap(new HashMap<>());

    private final AtomicInteger maxConnections = new AtomicInteger(0);

    /**
     * Creates a throttler that allows no connections.
     *
     * @param sessionHandler session handler
     *
     * @see HttpSessionHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    public SessionThrottler(final HttpSessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
        countsTowardsLimit = (exchange) -> true;
    }

    /**
     * Creates a throttler that limits the amount of simultaneous connections a user can have.
     *
     * @param sessionHandler session handler
     * @param maxConnections maximum simultaneous connections (per user)
     *
     * @see HttpSessionHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    public SessionThrottler(final HttpSessionHandler sessionHandler, final int maxConnections){
        this.sessionHandler = sessionHandler;
        countsTowardsLimit = (exchange) -> true;
        this.maxConnections.set(maxConnections);
    }

    /**
     * Creates a throttler that limits the amount of simultaneous connections a user can have and exempt connections.
     *
     * @param sessionHandler session handler
     * @param countsTowardsLimit determines which users are subject to the limit
     *
     * @see HttpSessionHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    public SessionThrottler(final HttpSessionHandler sessionHandler, final Predicate<HttpSession> countsTowardsLimit){
        this.sessionHandler = sessionHandler;
        this.countsTowardsLimit = countsTowardsLimit;
    }

    /**
     * Creates a throttler that limits the amount of simultaneous connections a user can have and exempt connections.
     *
     * @param sessionHandler session handler
     * @param countsTowardsLimit determines which users are subject to the limit
     * @param maxConnections maximum simultaneous connections (per user)
     */
    public SessionThrottler(final HttpSessionHandler sessionHandler, final Predicate<HttpSession> countsTowardsLimit, final int maxConnections){
        this.sessionHandler = sessionHandler;
        this.countsTowardsLimit = countsTowardsLimit;
        this.maxConnections.set(maxConnections);
    }

    /**
     * Returns the maximum allowed connections.
     *
     * @return maximum allowed connections (per user)
     *
     * @see #setMaxConnections(int)
     * @since 03.03.00
     * @author Ktt Development
     */
    public final int getMaxConnections(){
        return maxConnections.get();
    }

    /**
     * Sets the maximum allowed connections. Must be a positive number.
     *
     * @param maxConnections maximum allowed connections (per user)
     *
     * @see #getMaxConnections()
     * @since 03.03.00
     * @author Ktt Development
     */
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
                final AtomicInteger conn;
                if(!sessions.containsKey(session))
                    sessions.put(session,conn = new AtomicInteger(0));
                else
                    conn = sessions.get(session);
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

    @Override
    public String toString(){
        return
            "SessionThrottler"  + '{' +
            "condition"         + '=' +     countsTowardsLimit      + ", " +
            "sessions"          + '=' +     sessions.toString()     + ", " +
            "maxConnections"    + '=' +     maxConnections.get()    +
            '}';
    }

}
