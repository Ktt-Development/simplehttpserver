package ktt.lib.httpserver.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Implementation for {@link SimpleHttpServer}. Applications do not use this class.
 *
 * @see SimpleHttpServer
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
abstract class SimpleHttpServerImpl {

    /**
     * Creates a {@link SimpleHttpExchange}.
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
    static SimpleHttpServer createSimpleHttpServer(final int port, final int backlog) throws IOException {
        return new SimpleHttpServer() {

            private final HttpServer server = HttpServer.create();

            private final Map<HttpContext,HttpHandler> contexts = new HashMap<>();

            private boolean running = false;

            {
                server.bind(new InetSocketAddress(port),backlog);
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

            @Override
            public final HttpHandler getContextHandler(final String path){
                for(final HttpContext context : contexts.keySet()){
                    if(context.getPath().equalsIgnoreCase(getContext(path))){
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

        };
    }

    private static String getContext(final String path){
        final String linSlash = path.toLowerCase().replace("\\","/");
        final String seSlash = (!linSlash.startsWith("/") ? "/" : "") + linSlash + (!linSlash.endsWith("/") ? "/" : "");
        return seSlash.substring(0,seSlash.length()-1);
    }

}
