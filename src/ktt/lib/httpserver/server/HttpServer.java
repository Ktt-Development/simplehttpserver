package ktt.lib.httpserver.server;

import ktt.lib.httpserver.handler.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

@Deprecated
public abstract class HttpServer {

    HttpServer(){ }

//

    public static HttpServer create() throws IOException {
        return HttpServerImpl.createHttpServer(80, 0);
    }

    public static HttpServer create(int port) throws IOException {
        return HttpServerImpl.createHttpServer(port, 0);
    }

    public static HttpServer create(int port, int backlog) throws IOException {
        return HttpServerImpl.createHttpServer(port, backlog);
    }

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

    public abstract void removeContext(final String path);

    public abstract void removeContext(final HttpContext context);

    public abstract Map<HttpContext,HttpHandler> getContexts();

//

    public abstract void start();

    public abstract void stop();

    public abstract void stop(final int delay);

}
