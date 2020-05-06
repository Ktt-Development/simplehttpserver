package com.kttdevelopment.simplehttpserver.handler;

import com.sun.net.httpserver.HttpExchange;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Limits connections for the server.
 *
 * @see ThrottledHandler
 * @see SessionThrottler
 * @since 03.03.00
 * @version 03.03.00
 * @author Ktt Development
 */
public class ServerThrottler extends ConnectionThrottler {

    private final Predicate<HttpExchange> countsTowardsLimit;
    private final AtomicInteger connections = new AtomicInteger(0);

    private final AtomicInteger maxConnections = new AtomicInteger(0);

    public ServerThrottler(){
        countsTowardsLimit = exchange -> true;
    }

    public ServerThrottler(final int maxConnections){
        countsTowardsLimit = exchange -> true;
        this.maxConnections.set(maxConnections);
    }

    public ServerThrottler(final Predicate<HttpExchange> countsTowardsLimit){
        this.countsTowardsLimit = countsTowardsLimit;
    }

    public ServerThrottler(final Predicate<HttpExchange> countsTowardsLimit, final int maxConnections){
        this.countsTowardsLimit = countsTowardsLimit;
        this.maxConnections.set(maxConnections);
    }

    public final int getMaxConnections(){
        return maxConnections.get();
    }

    public synchronized final void setMaxConnections(final int maxConnections){
        if(maxConnections >= 0)
            this.maxConnections.set(maxConnections);
    }

    final boolean addConnection(final HttpExchange exchange){
        if(!countsTowardsLimit.test(exchange)){
            return true;
        }else{
            synchronized(this){
                if(connections.get() + 1 <= maxConnections.get()){
                    connections.incrementAndGet();
                    return true;
                }
            }
        }
        return false;
    }

    final void deleteConnection(final HttpExchange exchange){
        if(countsTowardsLimit.test(exchange))
            synchronized(this){
                connections.decrementAndGet();
            }
    }

    //

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString(){
        final StringBuilder OUT = new StringBuilder();

        OUT.append("ConnectionThrottler")   .append('{');
        OUT.append("condition")             .append('=')    .append(countsTowardsLimit)     .append(", ");
        OUT.append("connections")           .append('=')    .append(connections.get())      .append(", ");
        OUT.append("maxConnections")        .append('=')    .append(maxConnections.get());
        OUT.append('}');

        return OUT.toString();
    }

}
