package ktt.lib.httpserver.server;

import com.sun.net.httpserver.HttpContext;
import ktt.lib.httpserver.RequestHandler;
import ktt.lib.httpserver.handler.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

abstract class HttpServerProvider {

    static HttpServer createHttpServer(final int port, final int backlog) throws IOException{
        return new HttpServer() {

            private final com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            {

            }

        //

            @Override
            public synchronized final void bind(final int port) throws IOException{
                server.bind(port, 0);
            }

            @Override
            public void bind(final int port, final int backlog) throws IOException{

            }

        //

            @Override
            public InetSocketAddress getAddress(){
                return null;
            }

        //

            @Override
            public void setExecutor(final Executor executor){

            }

            @Override
            public Executor getExecutor(){
                return null;
            }

        //

            @Override
            public HttpContext createContext(final String path){
                return null;
            }

            @Override
            public HttpContext createContext(final String path, final HttpHandler handler){
                return null;
            }

            @Override
            public void removeContext(final String path){

            }

            @Override
            public void removeContext(final HttpContext context){

            }

        //

            @Override
            public Map<String, HttpHandler> getContexts(){
                return null;
            }

        //

            @Override
            public void start(){

            }

            @Override
            public void stop(){

            }

            @Override
            public void stop(final int delay){

            }
        }
    }

}
