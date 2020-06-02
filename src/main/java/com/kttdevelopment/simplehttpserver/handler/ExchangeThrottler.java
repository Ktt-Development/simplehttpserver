package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limits connections per address to the server.
 *
 * @see HttpExchange
 * @see ThrottledHandler
 * @see ServerThrottler
 * @see SessionThrottler
 * @since 03.05.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class ExchangeThrottler extends ConnectionThrottler {

    private final Map<InetAddress,AtomicInteger> connections = new ConcurrentHashMap<>();

    @Override
    final boolean addConnection(final HttpExchange exchange){
        final InetAddress address = exchange.getRemoteAddress().getAddress();
        final int maxConn = getMaxConnections(exchange);

        if(!connections.containsKey(address))
            connections.put(address,new AtomicInteger(0));

        final AtomicInteger conn = connections.get(address);

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
        final InetAddress address = exchange.getRemoteAddress().getAddress();
        if(connections.containsKey(address))
            connections.get(address).decrementAndGet();
    }

    @Override
    int getMaxConnections(final HttpExchange exchange){
        return -1;
    }

    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();

        OUT.append("ExchangeThrottler") .append('{');
        OUT.append("connections")       .append('=')    .append(connections.toString());
        OUT.append('}');

        return OUT.toString();
    }

}
