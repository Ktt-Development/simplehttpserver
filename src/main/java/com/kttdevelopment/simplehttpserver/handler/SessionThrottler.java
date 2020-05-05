package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.HttpSession;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class SessionThrottler extends ConnectionThrottler {

    private final SimpleHttpServer server;

    private final Predicate<HttpSession> usesLimit;
    private final Map<HttpSession, AtomicInteger> sessions = Collections.synchronizedMap(new HashMap<>());

    private int maxConnections = 0;

    public SessionThrottler(final SimpleHttpServer server){
        this.server = server;
        usesLimit = (exchange) -> true;
    }

    public SessionThrottler(final SimpleHttpServer server, final int maxConnections){
        this.server = server;
        usesLimit = (exchange) -> true;
        this.maxConnections = maxConnections;
    }

    public SessionThrottler(final SimpleHttpServer server, final Predicate<HttpSession> usesLimit){
        this.server = server;
        this.usesLimit = usesLimit;
    }

    public SessionThrottler(final SimpleHttpServer server, final Predicate<HttpSession> usesLimit, final int maxConnections){
        this.server = server;
        this.usesLimit = usesLimit;
        this.maxConnections = maxConnections;
    }

    @Override
    final boolean addConnection(final HttpExchange exchange){
        final HttpSession session = server.getHttpSession(exchange);
        if(!usesLimit.test(session)){
            return true;
        }else{
            synchronized(this){
                final AtomicInteger conn = sessions.get(session);
                if(conn.get() + 1 <= maxConnections){
                    conn.incrementAndGet();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    final void deleteConnection(final HttpExchange exchange){
        final HttpSession session = server.getHttpSession(exchange);
        if(usesLimit.test(session))
            synchronized(this){
                sessions.get(session).decrementAndGet();
            }
    }

}
