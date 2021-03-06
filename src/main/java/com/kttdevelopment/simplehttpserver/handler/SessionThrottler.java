/*
 * Copyright (C) 2021 Ktt Development
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.HttpSession;
import com.kttdevelopment.simplehttpserver.HttpSessionHandler;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limits connections per http session. This can be used to limit simultaneous downloads.
 *
 * @see HttpSession
 * @see ThrottledHandler
 * @see ExchangeThrottler
 * @see ServerExchangeThrottler
 * @see ServerSessionThrottler
 * @since 03.03.00
 * @version 03.05.01
 * @author Ktt Development
 */
public class SessionThrottler extends ConnectionThrottler {

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
    }

    @Override
    final boolean addConnection(final HttpExchange exchange){
        final HttpSession session = sessionHandler.getSession(exchange);
        final int maxConn = getMaxConnections(session, exchange);

        if(!connections.containsKey(session))
            connections.put(session, new AtomicInteger(0));

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
    public final int getMaxConnections(final HttpExchange exchange){
        return getMaxConnections(sessionHandler.getSession(exchange), exchange);
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

    //

    @Override
    public String toString(){
        return
            "SessionThrottler"  + '{' +
            "sessionHandler"    + '=' +     sessionHandler          + ", " +
            "connections"       + '=' +     connections.toString()  +
            '}';
    }

}
