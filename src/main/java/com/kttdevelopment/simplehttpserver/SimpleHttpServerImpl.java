package com.kttdevelopment.simplehttpserver;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Implementation for {@link SimpleHttpServer}. Applications do not use this class.
 *
 * @see SimpleHttpServer
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
abstract class SimpleHttpServerImpl {

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
     * @since 02.00.00
     * @author Ktt Development
     */
    static SimpleHttpServer createSimpleHttpServer(final Integer port, final Integer backlog) throws IOException {
        return new SimpleHttpServer() {

            private final HttpServer server = HttpServer.create();

            private final HashMap<HttpContext,HttpHandler> contexts = new HashMap<>();

            private boolean running = false;

            {
                if(port != null)
                    server.bind(new InetSocketAddress(port),backlog != null ? backlog : 0);
            }

        //

            @Override
            public final HttpServer getHttpServer(){
                return server;
            }

        //

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
                server.bind(addr,0);
            }

            @Override
            public synchronized final void bind(final InetSocketAddress addr, final int backlog) throws IOException{
                server.bind(addr,backlog);
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

        //

            @Override
            public synchronized final HttpContext createContext(final String path){
                final HttpContext context = server.createContext(getContext(path));
                contexts.put(context,context.getHandler());
                return context;
            }

            @Override
            public synchronized final HttpContext createContext(final String path, final HttpHandler handler){
                final HttpContext context = server.createContext(getContext(path),handler);
                contexts.put(context,handler);
                return context;
            }

        //

            private String generateRandomContext(){
                String targetContext;

                do targetContext = UUID.randomUUID().toString();
                    while(getContextHandler(targetContext) != null);

                return targetContext;
            }

            //

            @Override
            public synchronized final HttpContext createTemporaryContext(){
                return createTemporaryContext(generateRandomContext());
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final long maxTime){
                return createTemporaryContext(generateRandomContext(),maxTime);
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final HttpHandler handler){
                return createTemporaryContext(generateRandomContext(),handler);
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final HttpHandler handler, final long maxTime){
                return createTemporaryContext(generateRandomContext(),handler,maxTime);
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final String context){
                return createContext(context, (exchange) -> removeContext(context));
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final String context, final long maxTime){
                final HttpContext httpContext = createContext(context, (exchange) -> removeContext(context));

                new Thread(() -> {
                    try{
                        Thread.sleep(maxTime);
                    }catch(final InterruptedException ignored){ }
                    removeContext(httpContext);
                }).start();

                return httpContext;
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final String context, final HttpHandler handler){
                return createContext(context, (exchange) -> {
                    handler.handle(exchange);
                    removeContext(context);
                });
            }

            @Override
            public synchronized final HttpContext createTemporaryContext(final String context, final HttpHandler handler, final long maxTime){
                final HttpContext httpContext = createContext(context, (exchange) -> {
                    handler.handle(exchange);
                    removeContext(context);
                });

                new Thread(() -> {
                    try{
                        Thread.sleep(maxTime);
                    }catch(final InterruptedException ignored){ }
                    removeContext(httpContext);
                }).start();

                return httpContext;
            }

            //

            @Override
            public synchronized final void removeContext(final String path){
                server.removeContext(getContext(path));
                for(final HttpContext context : contexts.keySet()){
                    if(context.getPath().equalsIgnoreCase(getContext(path))){
                        contexts.remove(context);
                        break;
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
            public final HttpHandler getContextHandler(final String path){
                for(final HttpContext context : contexts.keySet()){
                    if(context.getPath().equals(getContext(path))){
                        return context.getHandler();
                    }
                }
                return null;
            }

            @Override
            public final HttpHandler getContextHandler(final HttpContext context){
                return contexts.get(context);
            }

            @Override
            public final Map<HttpContext, HttpHandler> getContexts(){
                return new HashMap<>(contexts);
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

        //

            @SuppressWarnings("StringBufferReplaceableByString")
            @Override
            public final String toString(){
                final StringBuilder OUT = new StringBuilder();
                OUT.append("SimpleHttpServer")  .append("{");
                OUT.append("httpServer")        .append("=")   .append(server)          .append(", ");
                OUT.append("contexts")          .append("=")   .append(contexts)        .append(", ");
                OUT.append("address")           .append("=")   .append(getAddress())    .append(", ");
                OUT.append("executor")          .append("=")   .append(getExecutor());
                OUT.append("}");
                return OUT.toString();
            }

        };
    }

    private static String getContext(final String path){
        final String linSlash = path.replace("\\","/");
        if(linSlash.equals("/")) return "/";
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

}
