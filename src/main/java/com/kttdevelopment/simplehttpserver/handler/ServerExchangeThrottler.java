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

import com.sun.net.httpserver.HttpExchange;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limits connections per address to the server and total server connections.
 *
 * @see HttpExchange
 * @see ThrottledHandler
 * @see ExchangeThrottler
 * @see SessionThrottler
 * @see ServerSessionThrottler
 * @since 03.05.00
 * @version 03.05.01
 * @author Ktt Development
 */
public class ServerExchangeThrottler extends ConnectionThrottler {

    private final Map<InetAddress,AtomicInteger> connections = new ConcurrentHashMap<>();

    private final AtomicInteger uConn = new AtomicInteger(0);
    private final AtomicInteger uConnMax = new AtomicInteger(-1);

    /**
     * Creates a throttler with connection limits on user and total connections.
     *
     * @since 03.05.00
     * @author Ktt Development
     */
    public ServerExchangeThrottler(){ }

    /**
     * Creates a throttler with connection limits on user and total connections.
     *
     * @param maxConnections maximum allowed server connections
     *
     * @since 03.05.00
     * @author Ktt Development
     */
    public ServerExchangeThrottler(final int maxConnections){
        uConnMax.set(maxConnections);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    final boolean addConnection(final HttpExchange exchange){
        final InetAddress address = exchange.getRemoteAddress().getAddress();
        final int maxConn = getMaxConnections(exchange);

        if(!connections.containsKey(address))
            connections.put(address, new AtomicInteger(0));

        final AtomicInteger conn = connections.get(address);
        final boolean exempt = canIgnoreConnectionLimit(exchange);

        if(maxConn < 0){
            if(!exempt){
                synchronized(this){
                    final int umax = uConnMax.get();
                    if(umax < 0 || uConn.get() < umax){
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
        final InetAddress address = exchange.getRemoteAddress().getAddress();
        if(connections.containsKey(address)){
            connections.get(address).decrementAndGet();
            if(!canIgnoreConnectionLimit(exchange))
                uConn.decrementAndGet();
        }
    }

    @Override
    public int getMaxConnections(final HttpExchange exchange){
        return -1;
    }

    /**
     * Returns if an exchange is exempt from the server connection limit only.
     *
     * @param exchange exchange to process
     * @return if exchange ignores server connection limit
     *
     * @see HttpExchange
     * @since 03.05.00
     * @author Ktt Development
     */
    @SuppressWarnings("SameReturnValue")
    public boolean canIgnoreConnectionLimit(final HttpExchange exchange){
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
            "ServerExchangeThrottler"   + '{' +
            "connections"               + '=' +     connections.toString()  + ", " +
            "userConnections"           + '=' +     uConn                   + ", " +
            "userConnectionsMax"        + '=' +     uConnMax +
            '}';
    }

}
