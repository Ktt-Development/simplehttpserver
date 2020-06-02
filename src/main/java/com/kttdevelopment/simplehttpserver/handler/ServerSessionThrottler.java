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
 * @see ThrottledHandler
 * @see SessionThrottler
 * @since 03.05.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class ServerSessionThrottler extends ConnectionThrottler{

    private final HttpSessionHandler sessionHandler;
    private final Map<HttpSession,AtomicInteger> connections = new ConcurrentHashMap<>();

    private final AtomicInteger uConn = new AtomicInteger(0);
    private final AtomicInteger uConnMax = new AtomicInteger(0);

    public ServerSessionThrottler(final HttpSessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
    }

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
        return -1;
    }

    /**
     * Returns if a session is exempt from the server connection limit only.
     *
     * @param exchange exchange to process
     * @return if exchange ignores server connection limit
     *
     * @see HttpSession
     * @see HttpExchange
     * @since 03.05.00
     * @author Ktt Development
     */
    boolean canIgnoreConnectionLimit(final HttpSession session, final HttpExchange exchange){
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

}
