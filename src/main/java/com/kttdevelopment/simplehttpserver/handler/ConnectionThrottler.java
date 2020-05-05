package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.HttpSession;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ConnectionThrottler {

    private final Predicate<HttpExchange> contributeToLimit;
    private final AtomicInteger           connections = new AtomicInteger(0);

    private int maxConnections = 0;

    public ConnectionThrottler(){
        contributeToLimit = (exchange) -> true;
    }

    public ConnectionThrottler(final Predicate<HttpExchange> counts){
        this.contributeToLimit = counts;
    }


    public synchronized final boolean addConnection(final HttpExchange exchange){
        if(!contributeToLimit.test(exchange)){
            return true;
        }else if(connections.get() + 1 <= maxConnections){
            connections.incrementAndGet();
            return true;
        }
        return false;
    }

    public synchronized final void deleteConnection(final HttpExchange exchange){
        if(contributeToLimit.test(exchange))
            connections.decrementAndGet();
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
