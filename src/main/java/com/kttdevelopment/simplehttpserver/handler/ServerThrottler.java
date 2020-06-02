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
 * @version 03.05.00
 * @author Ktt Development
 */
public class ServerThrottler extends ConnectionThrottler {

    private final Predicate<HttpExchange> countsTowardsLimit;
    private final AtomicInteger connections = new AtomicInteger(0);

    private final AtomicInteger maxConnections = new AtomicInteger(0);

    /**
     * Creates a throttler that allows no connections.
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public ServerThrottler(){
        countsTowardsLimit = exchange -> true;
    }

    /**
     * Creates a throttler that allows a certain amount of simultaneous connections.
     *
     * @param maxConnections maximum simultaneous connections
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public ServerThrottler(final int maxConnections){
        countsTowardsLimit = exchange -> true;
        this.maxConnections.set(maxConnections);
    }

    /**
     * Creates a throttler that allows a certain amount of simultaneous connections and exempt connections.
     *
     * @param countsTowardsLimit determines which exchanges count towards the connection limit
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public ServerThrottler(final Predicate<HttpExchange> countsTowardsLimit){
        this.countsTowardsLimit = countsTowardsLimit;
    }

    /**
     * Creates a throttler that allows a certain amount of simultaneous connections and exempt connections.
     *
     * @param countsTowardsLimit determines which exchanges count towards the connection limit
     * @param maxConnections maximum simultaneous connections
     *
     * @since 03.03.00
     * @author Ktt Development
     */
    public ServerThrottler(final Predicate<HttpExchange> countsTowardsLimit, final int maxConnections){
        this.countsTowardsLimit = countsTowardsLimit;
        this.maxConnections.set(maxConnections);
    }

    /**
     * Returns the maximum allowed connections.
     *
     * @return maximum allowed connections
     *
     * @see #setMaxConnections(int)
     * @since 03.03.00
     * @author Ktt Development
     */
    public final int getMaxConnections(){
        return maxConnections.get();
    }

    /**
     * Sets the maximum allowed connections. Must be a positive number.
     *
     * @param maxConnections maximum allowed connections
     *
     * @see #getMaxConnections()
     * @since 03.03.00
     * @author Ktt Development
     */
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

    @Override
    public String toString(){
        return
            "ConnectionThrottler"   + '{' +
            "condition"             + '=' +     countsTowardsLimit      + ", " +
            "connections"           + '=' +     connections.get()       + ", " +
            "maxConnections"        + '=' +     maxConnections.get()    +
            '}';
    }

}
