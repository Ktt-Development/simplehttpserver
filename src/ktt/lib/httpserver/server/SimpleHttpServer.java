package ktt.lib.httpserver.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

public abstract class SimpleHttpServer {

    SimpleHttpServer(){ }

//

    public static SimpleHttpServer create() throws IOException {
        return SimpleHttpServerImpl.createSimpleHttpServer(80,0);
    }

    public static SimpleHttpServer create(final int port) throws IOException {
        return SimpleHttpServerImpl.createSimpleHttpServer(port,0);
    }

    public static SimpleHttpServer create(final int port, final int backlog) throws IOException {
        return SimpleHttpServerImpl.createSimpleHttpServer(port,backlog);
    }

//

    public abstract HttpServer getHttpServer();

//

    public abstract InetSocketAddress bind(final int port) throws IOException;

    public abstract InetSocketAddress bind(final int port, final int backlog) throws IOException;

    public abstract void bind(final InetSocketAddress addr) throws IOException;

    public abstract void bind(final InetSocketAddress addr, final int backlog) throws IOException;

//

    public abstract InetSocketAddress getAddress();

//

    public abstract void setExecutor(final Executor executor);

    public abstract Executor getExecutor();

//

    public abstract HttpContext createContext(final String path);

    public abstract HttpContext createContext(final String path, HttpHandler handler);

    public abstract HttpHandler getContextHandler(final String path);

    public abstract HttpHandler getContextHandler(final HttpContext context);

    public abstract void removeContext(final String path);

    public abstract void removeContext(final HttpContext context);

    public abstract Map<HttpContext,HttpHandler> getContexts();

//

    public abstract void start();

    public abstract void stop();

    public abstract void stop(final int delay);

}
