package ktt.lib.httpserver.server;

import ktt.lib.httpserver.handler.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

abstract class HttpServerImpl {

    static HttpServer createHttpServer(final int port, final int backlog) throws IOException{
        return new HttpServer() {

            private final com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            private final Map<HttpContext,HttpHandler> contexts = new HashMap<>();

            private boolean running = false;

            {

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
            public synchronized void bind(final InetSocketAddress addr) throws IOException{
                server.bind(addr, 0);
            }

            @Override
            public synchronized void bind(final InetSocketAddress addr, final int backlog) throws IOException{
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

        //

            @Override
            public synchronized final HttpContext createContext(final String path){

            }

            @Override
            public synchronized final HttpContext createContext(final String path, final HttpHandler handler){

            }

            @Override
            public synchronized final void removeContext(final String path){

            }

            @Override
            public synchronized final void removeContext(final HttpContext context){

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
                    server.stop(delay);
                    running = false;
                }
            }

        };
    }

}
