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

package com.kttdevelopment.simplehttpserver;

import com.kttdevelopment.simplehttpserver.handler.RootHandler;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Implementation for {@link SimpleHttpsServer}. Applications do not use this class.
 *
 * @see SimpleHttpsServer
 * @since 03.04.00
 * @version 03.05.08
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
final class SimpleHttpsServerImpl extends SimpleHttpsServer {

    private final HttpsServer server = HttpsServer.create();

    private HttpSessionHandler sessionHandler;

    private final Map<HttpContext,HttpHandler> contexts = new HashMap<>();

    private boolean running = false;

    /**
     * Creates a {@link SimpleHttpServer}.
     *
     * @param port port to run the server on
     * @param backlog how many requests to backlog
     * @return a {@link SimpleHttpServer}
     * @throws java.net.BindException if server can not bind to port
     * @throws IOException uncaught exception
     *
     * @see SimpleHttpServer
     * @since 03.04.00
     * @author Ktt Development
     */
    static SimpleHttpsServer createSimpleHttpsServer(final Integer port, final Integer backlog) throws IOException{
        return new SimpleHttpsServerImpl(port, backlog);
    }

    SimpleHttpsServerImpl(final Integer port, final Integer backlog) throws IOException{
        if(port != null)
            server.bind(new InetSocketAddress(port), backlog != null ? backlog : 0);
    }

    private void handle(final HttpExchange exchange){
        if(sessionHandler != null)
            sessionHandler.getSession(exchange).updateLastAccessTime();
    }

//

    @Override
    public final HttpsServer getHttpServer(){
        return server;
    }

//

    @Override
    public final void setHttpsConfigurator(final HttpsConfigurator config){
        server.setHttpsConfigurator(config);
    }

    @Override
    public final HttpsConfigurator getHttpsConfigurator(){
        return server.getHttpsConfigurator();
    }

// region copy

    @Override
    public synchronized final InetSocketAddress bind(final int port) throws IOException{
        final InetSocketAddress addr = new InetSocketAddress(port);
        server.bind(addr, 0);
        return addr;
    }

    @Override
    public synchronized final InetSocketAddress bind(final int port, final int backlog) throws IOException{
        final InetSocketAddress addr = new InetSocketAddress(port);
        server.bind(addr, backlog);
        return addr;
    }

    @Override
    public synchronized final void bind(final InetSocketAddress addr) throws IOException{
        server.bind(addr, 0);
    }

    @Override
    public synchronized final void bind(final InetSocketAddress addr, final int backlog) throws IOException{
        server.bind(addr, backlog);
    }

//

    @Override
    public final InetSocketAddress getAddress(){
        return server.getAddress();
    }

//

    @Override
    public synchronized final void setExecutor(final Executor executor){
        server.setExecutor(executor);
    }

    @Override
    public final Executor getExecutor(){
        return server.getExecutor();
    }

    @Override
    public synchronized final void setHttpSessionHandler(final HttpSessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
    }

    @Override
    public final HttpSessionHandler getHttpSessionHandler(){
        return sessionHandler;
    }

    @Override
    public final HttpSession getHttpSession(final HttpExchange exchange){
        return sessionHandler != null ? sessionHandler.getSession(exchange) : null;
    }

    @Override
    public final HttpSession getHttpSession(final SimpleHttpExchange exchange){
        return getHttpSession(exchange.getHttpExchange());
    }

    //

    @Override
    public synchronized final HttpContext createContext(final String context){
        return createContext(context, HttpExchange::close, null);
    }

    @Override
    public synchronized final HttpContext createContext(final String context, final HttpHandler handler){
        return createContext(context, handler, null);
    }

    //

    @Override
    public synchronized final HttpContext createContext(final String context, final Authenticator authenticator){
        return createContext(context, HttpExchange::close, authenticator);
    }

    @Override
    public synchronized final HttpContext createContext(final String context, final HttpHandler handler, final Authenticator authenticator){
        final String ct = ContextUtil.getContext(context, true, false);
        if(!ct.equals("/") && handler instanceof RootHandler)
            throw new IllegalArgumentException("RootHandler can only be used at the root '/' context");

        final HttpHandler wrapper = exchange -> {
            handle(exchange);
            handler.handle(exchange);
        };

        final HttpContext hc = server.createContext(ct);

        hc.setHandler(wrapper);
        contexts.put(hc, handler);

        if(authenticator != null)
            hc.setAuthenticator(authenticator);

        return hc;
    }

    //

    @SuppressWarnings("CaughtExceptionImmediatelyRethrown")
    @Override
    public synchronized final void removeContext(final String context){
        try{
            server.removeContext(ContextUtil.getContext(context, true, false));
        }catch(final IllegalArgumentException e){
            throw e;
        }finally{
            for(final HttpContext hc : contexts.keySet()){
                if(hc.getPath().equalsIgnoreCase(ContextUtil.getContext(context, true, false))){
                    contexts.remove(hc);
                    break;
                }
            }
        }
    }

    @Override
    public synchronized final void removeContext(final HttpContext context){
        contexts.remove(context);
        server.removeContext(context);
    }

//

    @Override
    public final HttpHandler getContextHandler(final String context){
        for(final Map.Entry<HttpContext,HttpHandler> entry : contexts.entrySet())
            if(entry.getKey().getPath().equals(ContextUtil.getContext(context, true, false)))
                return entry.getValue();
        return null;
    }

    @Override
    public final HttpHandler getContextHandler(final HttpContext context){
        return contexts.get(context);
    }

    @Override
    public final Map<HttpContext,HttpHandler> getContexts(){
        return new HashMap<>(contexts);
    }

    //

    @Override
    public synchronized final String getRandomContext(){
        return getRandomContext("");
    }

    @Override
    public synchronized final String getRandomContext(final String context){
        String targetContext;

        final String head = context.isEmpty() ? "" : ContextUtil.getContext(context, true, false);

        do targetContext = head + ContextUtil.getContext(UUID.randomUUID().toString(), true, false);
            while(getContextHandler(targetContext) != null);

        return targetContext;
    }

    //

    @Override
    public synchronized final void start(){
        if(!running){
            server.start();
            running = true;
        }
    }

    @Override
    public synchronized final void stop(){
        stop(0);
    }

    @Override
    public synchronized final void stop(final int delay){
        if(running){
            running = false;
            server.stop(delay);
        }
    }

// endregion copy

    @Override
    public String toString(){
        return
            "SimpleHttpsServer" + '{' +
            "httpServer"        + '=' +     server                  + ", " +
            "httpsConfigurator" + '=' +     getHttpsConfigurator()  + ", " +
            "contexts"          + '=' +     contexts                + ", " +
            "address"           + '=' +     getAddress()            + ", " +
            "executor"          + '=' +     getExecutor()           +
            '}';
    }

}
