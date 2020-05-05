package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ServerThrottler extends ConnectionThrottler {

    private final Predicate<HttpExchange> usesLimit;
    private final AtomicInteger connections = new AtomicInteger(0);

    private int maxConnections = 0;

    public ServerThrottler(){
        usesLimit = exchange -> true;
    }

    public ServerThrottler(final int maxConnections){
        usesLimit = exchange -> true;
        this.maxConnections = maxConnections;
    }

    public ServerThrottler(final Predicate<HttpExchange> counts){
        this.usesLimit = counts;
    }

    public ServerThrottler(final Predicate<HttpExchange> contributeToLimit, final int maxConnections){
        this.usesLimit = contributeToLimit;
        this.maxConnections = maxConnections;
    }

    final boolean addConnection(final HttpExchange exchange){
        if(!usesLimit.test(exchange)){
            return true;
        }else{
            synchronized(this){
                if(connections.get() + 1 <= maxConnections){
                    connections.incrementAndGet();
                    return true;
                }
            }
        }
        return false;
    }

    final void deleteConnection(final HttpExchange exchange){
        if(usesLimit.test(exchange))
            synchronized(this){
                connections.decrementAndGet();
            }
    }

    //

    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();

        OUT.append("ConnectionThrottler")   .append('{');
        OUT.append("connections")           .append('=')    .append(connections)        .append(", ");
        OUT.append("maxConnections")        .append('=')    .append(maxConnections);
        OUT.append('}');

        return OUT.toString();
    }

}
