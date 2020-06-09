package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.HttpSession;
import com.kttdevelopment.simplehttpserver.HttpSessionHandler;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limits connections per session to the server and total server sessions.
 *
 * @see HttpSession
 * @see HttpSessionHandler
 * @see ThrottledHandler
 * @see ExchangeThrottler
 * @see ServerExchangeThrottler
 * @see SessionThrottler
 * @since 03.05.00
 * @version 03.05.01
 * @author Ktt Development
 */
public class ServerSessionThrottler extends ConnectionThrottler{

    private final HttpSessionHandler sessionHandler;
    private final Map<HttpSession,AtomicInteger> connections = new ConcurrentHashMap<>();

    private final AtomicInteger uConn = new AtomicInteger(0);
    private final AtomicInteger uConnMax = new AtomicInteger(-1);

    /**
     * Creates a throttler with limits on session and total connections.
     *
     * @param sessionHandler http session handler
     *
     * @see HttpSessionHandler
     * @since 03.05.00
     * @author Ktt Development
     */
    public ServerSessionThrottler(final HttpSessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
    }

    /**
     * Creates a throttler with limits on session and total connections.
     *
     * @param sessionHandler http session handler
     * @param maxConnections maximum allowed server connections
     *
     * @see HttpSessionHandler
     * @since 03.05.00
     * @author Ktt Development
     */
    public ServerSessionThrottler(final HttpSessionHandler sessionHandler, final int maxConnections){
        this.sessionHandler = sessionHandler;
        uConnMax.set(maxConnections);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    final boolean addConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        final int maxConn = getMaxConnections(session,exchange);

        if(!connections.containsKey(session))
            connections.put(session,new AtomicInteger(0));

        final AtomicInteger conn = connections.get(session);
        final boolean exempt = canIgnoreConnectionLimit(session,exchange);

        if(maxConn < 0){
            if(!exempt){
                synchronized(this){
                    final int uMax = uConnMax.get();
                    if(uMax < 0 || uConn.get() < uMax){
                        conn.incrementAndGet();
                        uConn.incrementAndGet();
                        return true;
                    }
                    return false;
                }
            }else{
                conn.incrementAndGet();
                return true;
            }
        }else{
            if(!exempt){
                synchronized(this){
                    final int umax = uConnMax.get();
                    if(conn.get() < maxConn && (umax < 0 || uConn.get() < umax)){
                        conn.incrementAndGet();
                        uConn.incrementAndGet();
                        return true;
                    }
                    return false;
                }
            }else{
                final AtomicBoolean added = new AtomicBoolean(false);
                conn.updateAndGet(operand -> {
                    if(operand < maxConn) added.set(true);
                    return operand < maxConn ? operand + 1 : operand;
                });
                return added.get();
            }
        }
    }

    @Override
    final void deleteConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        if(connections.containsKey(session)){
            connections.get(session).decrementAndGet();
            if(!canIgnoreConnectionLimit(session,exchange))
                uConn.decrementAndGet();
        }
    }

    @Override
    public final int getMaxConnections(final HttpExchange exchange){
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
    public int getMaxConnections(final HttpSession session, final HttpExchange exchange){
        return -1;
    }

    /**
     * Returns if a session is exempt from the server connection limit only.
     *
     * @param session session to process
     * @param exchange exchange to process
     * @return if exchange ignores server connection limit
     *
     * @see HttpSession
     * @see HttpExchange
     * @since 03.05.00
     * @author Ktt Development
     */
    @SuppressWarnings("SameReturnValue")
    public boolean canIgnoreConnectionLimit(final HttpSession session, final HttpExchange exchange){
        return false;
    }

    /**
     * Sets the maximum number of connections the server can have. A value of <code>-1</code> means unlimited connections.
     *
     * @param connections maximum number of connections allowed on the server
     *
     * @see #getMaxConnections(HttpExchange)
     * @since 03.05.00
     * @author Ktt Development
     */
    public synchronized final void setMaxServerConnections(final int connections){
        uConnMax.set(connections);
    }

    /**
     * Returns the maximum number of connections the server can have.
     *
     * @return maximum number of connections allowed on th server
     *
     * @see #setMaxServerConnections(int)
     * @since 03.05.00
     * @author Ktt Development
     */
    public synchronized final int getMaxServerConnections(){
        return uConnMax.get();
    }

    @Override
    public String toString(){
        return
            "ServerSessionThrottler"    + '{' +
            "sessionHandler"            + '=' +     sessionHandler          + ", " +
            "connections"               + '=' +     connections.toString()  + ", " +
            "userConnections"           + '=' +     uConn                   + ", " +
            "userConnectionsMax"        + '=' +     uConnMax +
            '}';
    }

}
