package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.*;
import com.sun.net.httpserver.HttpExchange;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Limits connections per http session. This can be used to limit simultaneous downloads.
 *
 * @see HttpSession
 * @see ThrottledHandler
 * @see ExchangeThrottler
 * @see ServerThrottler
 * @since 03.03.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class SessionThrottler extends ConnectionThrottler {

    @Deprecated
    private final Predicate<HttpSession> countsTowardsLimit;
    @Deprecated
    private final AtomicInteger maxConnections = new AtomicInteger(0);


    private final HttpSessionHandler sessionHandler;
    private final Map<HttpSession,AtomicInteger> connections = new ConcurrentHashMap<>();


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
     * @deprecated Override {@link #getMaxConnections(HttpSession, HttpExchange)} for max connections
     * @param sessionHandler session handler
     * @param maxConnections maximum simultaneous connections (per user)
     *
     * @see HttpSessionHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    @Deprecated
    public SessionThrottler(final HttpSessionHandler sessionHandler, final int maxConnections){
        this.sessionHandler = sessionHandler;
        countsTowardsLimit = (exchange) -> true;
        this.maxConnections.set(maxConnections);
    }

    /**
     * Creates a throttler that limits the amount of simultaneous connections a user can have and exempt connections.
     *
     * @deprecated Override {@link #getMaxConnections(HttpSession, HttpExchange)} for connection limit
     * @param sessionHandler session handler
     * @param countsTowardsLimit determines which users are subject to the limit
     *
     * @see HttpSessionHandler
     * @since 03.03.00
     * @author Ktt Development
     */
    @Deprecated
    public SessionThrottler(final HttpSessionHandler sessionHandler, final Predicate<HttpSession> countsTowardsLimit){
        this.sessionHandler = sessionHandler;
        this.countsTowardsLimit = countsTowardsLimit;
    }

    /**
     * Creates a throttler that limits the amount of simultaneous connections a user can have and exempt connections.
     *
     * @deprecated Override {{@link #getMaxConnections(HttpSession, HttpExchange)} for connection limit and max connections
     * @param sessionHandler session handler
     * @param countsTowardsLimit determines which users are subject to the limit
     * @param maxConnections maximum simultaneous connections (per user)
     */
    @Deprecated
    public SessionThrottler(final HttpSessionHandler sessionHandler, final Predicate<HttpSession> countsTowardsLimit, final int maxConnections){
        this.sessionHandler = sessionHandler;
        this.countsTowardsLimit = countsTowardsLimit;
        this.maxConnections.set(maxConnections);
    }

    /**
     * Returns the maximum allowed connections.
     *
     * @deprecated Use {@link #getMaxConnections(HttpSession, HttpExchange)} instead
     * @return maximum allowed connections (per user)
     *
     * @see #setMaxConnections(int)
     * @since 03.03.00
     * @author Ktt Development
     */
    @Deprecated
    public final int getMaxConnections(){
        return maxConnections.get();
    }

    /**
     * Sets the maximum allowed connections. Must be a positive number.
     *
     * @deprecated Override {@link #getMaxConnections(HttpSession, HttpExchange)} instead
     * @param maxConnections maximum allowed connections (per user)
     *
     * @see #getMaxConnections()
     * @since 03.03.00
     * @author Ktt Development
     */
    @Deprecated
    public synchronized final void setMaxConnections(final int maxConnections){
        if(maxConnections >= 0)
            this.maxConnections.set(maxConnections);
    }

    @Override
    final boolean addConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        final int maxConn = getMaxConnections(session,exchange);

        if(!connections.containsKey(session))
            connections.put(session,new AtomicInteger(0));

        final AtomicInteger conn = connections.get(session);

        if(maxConn < 0){
            conn.incrementAndGet();
            return true;
        }else{
            final AtomicBoolean added = new AtomicBoolean(false);
            conn.updateAndGet(operand -> {
                if(operand < maxConn) added.set(true);
                return operand < maxConn ? operand + 1 : operand;
            });
            return added.get();
        }
    }

    @Override
    final void deleteConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        if(connections.containsKey(session))
            connections.get(session).decrementAndGet();
    }

    @Override
    final int getMaxConnections(final HttpExchange exchange){
        return getMaxConnections(sessionHandler.getSession(exchange),exchange);
    }

    /**
     * Returns the maximum number of connections for a session. A value of <code>-1</code> means unlimited connections.
     *
     * @param session session to process
     * @param exchange exchange to process
     * @return maximum number of connections allowed
     *
     * @since 03.05.00
     * @author Ktt Development
     */
    int getMaxConnections(final HttpSession session, final HttpExchange exchange){
        return countsTowardsLimit.test(session) ? getMaxConnections() : -1;
    }

    //

    @Override
    public String toString(){
        return
            "SessionThrottler"              + '{' +
            "condition@depreciated"         + '=' +     countsTowardsLimit      + ", " +
            "sessions@depreciated"          + '=' +     connections.toString()  + ", " +
            "maxConnections@depreciated"    + '=' +     maxConnections.get()    + ", " +
            "sessionHandler"                + '=' +     sessionHandler          + ", " +
            "connections"                   + '=' +     connections.toString()  + ", " +
            '}';
    }

}
