package ktt.lib.httpserver.server;

import com.sun.net.httpserver.HttpContext;
import ktt.lib.httpserver.RequestHandler;
import ktt.lib.httpserver.handler.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

public abstract class HttpServer {

    HttpServer(){ }

//

    public static HttpServer create() throws IOException {

    }

    public static HttpServer create(int port) throws IOException {

    }

    public static HttpServer create(int port, int backlog) throws IOException {

    }

//

    public abstract void bind(final int port) throws IOException;

    public abstract void bind(final int port, final int backlog) throws IOException;

    public abstract void bind(final InetSocketAddress addr) throws IOException;

    public abstract void bind(final InetSocketAddress addr, final int backlog) throws IOException;

//

    public abstract InetSocketAddress getAddress();

//

    public abstract void setExecutor(final Executor executor);

    public abstract Executor getExecutor();

//

    // todo              ↓
    public abstract HttpContext createContext(final String path);

    // todo              ↓                                            ↓
    public abstract HttpContext createContext(final String path, HttpHandler handler);

    public abstract void removeContext(final String path);

    // todo                                       ↓
    public abstract void removeContext(final HttpContext context);

    // todo                           ↓
    public abstract Map<String,HttpHandler> getContexts();

//

    public abstract void start();

    public abstract void stop();

    public abstract void stop(final int delay);

}
