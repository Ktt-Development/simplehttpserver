package ktt.lib.httpserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <i>This class is a simplified implementation of {@link HttpServer}.</i><br>
 * At least one {@link HttpHandler} must be created in order to process requests. When handling requests the server will use the most specific context. If no handler can be found it is rejected with a 404 response.
 *
 * @see HttpServer
 * @see HttpHandler
 * @see SimpleHttpHandler
 * @since 02.00.00
 * @version 02.00.00
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class SimpleHttpServer {

    /**
     * Create an empty {@link SimpleHttpServer}. Applications don't use this method.
     *
     * @see SimpleHttpServerImpl#createSimpleHttpServer(Integer, Integer)
     * @since 02.00.00
     * @author Ktt Development
     */
    SimpleHttpServer(){ }

//

    /**
     * Creates a {@link SimpleHttpServer}.
     *
     * @return a {@link SimpleHttpServer}
     * @throws IOException uncaught exception
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public static SimpleHttpServer create() throws IOException {
        return SimpleHttpServerImpl.createSimpleHttpServer(null,null);
    }

    /**
     * Creates a {@link SimpleHttpServer} bounded to a port.
     *
     * @param port port to bind to
     * @return a {@link SimpleHttpServer}
     * @throws java.net.BindException if server can not bind to port
     * @throws NullPointerException if address is <code>null</code>
     * @throws IllegalArgumentException if port is out of range
     * @throws IOException uncaught exception
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public static SimpleHttpServer create(final int port) throws IOException {
        return SimpleHttpServerImpl.createSimpleHttpServer(port,null);
    }

    /**
     * Creates a {@link SimpleHttpServer} bounded to a port.
     *
     * @param port port to bind to
     * @param backlog request backlog
     * @return a {@link SimpleHttpServer}
     * @throws java.net.BindException if server can not bind to port
     * @throws NullPointerException if address is <code>null</code>
     * @throws IllegalArgumentException if port is out of range
     * @throws IOException uncaught exception
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public static SimpleHttpServer create(final int port, final int backlog) throws IOException {
        return SimpleHttpServerImpl.createSimpleHttpServer(port,backlog);
    }

//

    /**
     * Returns the native http server.
     *
     * @return http server
     *
     * @see HttpServer
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpServer getHttpServer();

//

    /**
     * Binds the server to a port.
     *
     * @param port port to bind the server to
     * @return address the server is binded to
     * @throws java.net.BindException if server could not be bound to port, or if it's already bound
     * @throws IllegalArgumentException if port is out of range
     * @throws NullPointerException if address is <code>null</code>
     * @throws IOException uncaught exception
     *
     * @see #bind(int, int)
     * @see #bind(InetSocketAddress)
     * @see #bind(InetSocketAddress, int)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract InetSocketAddress bind(final int port) throws IOException;

    /**
     * Binds the server to a port.
     *
     * @param port port to bind the server to
     * @param backlog request backlog
     * @return address the server is binded to
     * @throws java.net.BindException if server could not be bound to port, or if it's already bound
     * @throws IllegalArgumentException if port is out of range
     * @throws NullPointerException if address is <code>null</code>
     * @throws IOException uncaught exception
     *
     * @see #bind(int)
     * @see #bind(InetSocketAddress)
     * @see #bind(InetSocketAddress, int)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract InetSocketAddress bind(final int port, final int backlog) throws IOException;

    /**
     * Binds the server to a port.
     *
     * @param addr address to bind the server to
     * @throws java.net.BindException if server could not be bound to port, or if it's already bound
     * @throws IllegalArgumentException if port is out of range
     * @throws NullPointerException if address is <code>null</code>
     * @throws IOException uncaught exception
     *
     * @see InetSocketAddress
     * @see #bind(int)
     * @see #bind(int, int)
     * @see #bind(InetSocketAddress, int)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void bind(final InetSocketAddress addr) throws IOException;

    /**
     * Binds the server to a port.
     *
     * @param addr address to bind the server to
     * @param backlog request backlog
     * @throws java.net.BindException if server could not be bound to port, or if it's already bound
     * @throws NullPointerException if address is <code>null</code>
     * @throws IOException uncaught exception
     *
     * @see InetSocketAddress
     * @see #bind(int)
     * @see #bind(int, int)
     * @see #bind(InetSocketAddress)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void bind(final InetSocketAddress addr, final int backlog) throws IOException;

//

    /**
     * Returns the address that the server is binded to.
     *
     * @return binded address
     *
     * @see InetSocketAddress
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract InetSocketAddress getAddress();

//

    /**
     * Sets the server's executor. <br>
     * For unlimited simultaneous threading use {@link java.util.concurrent.Executors#newCachedThreadPool()}; for limited simultaneous threading use {@link java.util.concurrent.Executors#newFixedThreadPool(int)}. <br>
     *
     * @param executor server executor
     *
     * @see #getExecutor()
     * @see Executor
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void setExecutor(final Executor executor);

    /**
     * Returns the server's executor or null if none exists.
     *
     * @return server executor
     *
     * @see #setExecutor(Executor)
     * @see Executor
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Executor getExecutor();

//

    /**
     * Creates an empty context.
     *
     * @param context the context
     * @return the http context associated with the context
     * @throws IllegalArgumentException if the context is invalid or taken
     * @throws NullPointerException if the context is null
     *
     * @see HttpContext
     * @see #createContext(String, HttpHandler)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createContext(final String context);

    /**
     * Creates a context mapped to a specified {@link HttpHandler}.
     *
     * @param context the context
     * @param handler the handler
     * @return the http context associated with the context
     * @throws IllegalArgumentException if the context is invalid or taken
     * @throws NullPointerException if the context is null
     *
     * @see HttpContext
     * @see HttpHandler
     * @see #createContext(String)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createContext(final String context, final HttpHandler handler);

//

    /**
     * Creates a temporary context at a random address that will remove itself after the first connection. This type of context is typically used for single use downloads or media file hosting. <br>
     * To get the context use {@link HttpContext#getPath()}.
     *
     * @return the http context associated with the context
     *
     * @see HttpContext
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext();

    /**
     * Creates a temporary context at a random address that will remove itself after the first connection or after the max time is passed. This type of context is typically used for single use downloads or media file hosting. <br>
     * To get the context use {@link HttpContext#getPath()}.
     *
     * @param maxTime the maximum time the context may exist for (in milliseconds)
     * @return the http context associated with the context
     *
     * @see HttpContext
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final long maxTime);

    /**
     * Creates a temporary context at a random address that will remove itself after the first connection. This type of context is typically used for single use downloads or media file hosting. <br>
     * To get the context use {@link HttpContext#getPath()}. <br>
     * This method encapsulates the handler with a temporary one, so {@link HttpContext#getHandler()} will not return the one passed in the parameter, instead it will return the encapsulating handler.
     *
     * @param handler handler to use
     * @return the http context associated with the context
     *
     * @see HttpContext
     * @see HttpHandler
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final HttpHandler handler);

    /**
     * Creates a temporary context at a random address that will remove itself after the first connection or after the max time is passed. This type of context is typically used for single use downloads or media file hosting. <br>
     * To get the context use {@link HttpContext#getPath()}. <br>
     * This method encapsulates the handler with a temporary one, so {@link HttpContext#getHandler()} will not return the one passed in the parameter, instead it will return the encapsulating handler.
     *
     * @param handler handler to use
     * @param maxTime the maximum time the context may exist for (in milliseconds)
     * @return the http context associated with the context
     *
     * @see HttpContext
     * @see HttpHandler
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final HttpHandler handler, final long maxTime);

    /**
     * Creates a temporary context at a specified address that will remove itself after the first connection. This type of context is typically used for single use downloads or media file hosting. <br>
     *
     * @param context the context
     * @return the http context associated with the context
     * @throws IllegalArgumentException if the context is invalid or taken
     * @throws NullPointerException if the context is null
     *
     * @see HttpContext
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final String context);

    /**
     * Creates a temporary context at a specified address that will remove itself after the first connection or after the max time is passed. This type of context is typically used for single use downloads or media file hosting. <br>
     *
     * @param context the context
     * @param maxTime the maximum time the context may exist for (in milliseconds)
     * @return the http context associated with the context
     * @throws IllegalArgumentException if the context is invalid or taken
     * @throws NullPointerException if the context is null
     *
     * @see HttpContext
     * @see HttpHandler
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final String context, final long maxTime);

    /**
     * Creates a temporary context at a specified address that will remove itself after the first connection. This type of context is typically used for single use downloads or media file hosting. <br>
     * This method encapsulates the handler with a temporary one, so {@link HttpContext#getHandler()} will not return the one passed in the parameter, instead it will return the encapsulating handler.
     *
     * @param context the context
     * @param handler handler to use
     * @return the http context associated with the context
     * @throws IllegalArgumentException if the context is invalid or taken
     * @throws NullPointerException if the context is null
     *
     * @see HttpContext
     * @see HttpHandler
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler, long)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final String context, final HttpHandler handler);


    /**
     * Creates a temporary context at a random address that will remove itself after the first connection or after the max time is passed. This type of context is typically used for single use downloads or media file hosting. <br>
     * To get the context use {@link HttpContext#getPath()}. <br>
     * This method encapsulates the handler with a temporary one, so {@link HttpContext#getHandler()} will not return the one passed in the parameter, instead it will return the encapsulating handler.
     *
     * @param context the context
     * @param handler handler to use
     * @param maxTime the maximum time the context may exist for (in milliseconds)
     * @return the http context associated with the context
     * @throws IllegalArgumentException if the context is invalid or taken
     * @throws NullPointerException if the context is null
     *
     * @see HttpContext
     * @see HttpHandler
     * @see #createTemporaryContext()
     * @see #createTemporaryContext(long)
     * @see #createTemporaryContext(HttpHandler)
     * @see #createTemporaryContext(HttpHandler, long)
     * @see #createTemporaryContext(String)
     * @see #createTemporaryContext(String, long)
     * @see #createTemporaryContext(String, HttpHandler)
     * @see #removeContext(String)
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext createTemporaryContext(final String context, final HttpHandler handler, final long maxTime);

//

    /**
     * Removes the context at the context.
     *
     * @param context the context to remove
     * @throws IllegalArgumentException if no handler at that context exists
     * @throws NullPointerException if the context is null
     *
     * @see #removeContext(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void removeContext(final String context);

    /**
     * Removes the context from the server.
     * @param context the context to remove
     * @throws IllegalArgumentException if no handler at that context exists
     * @throws NullPointerException if the context is null
     *
     * @see #removeContext(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void removeContext(final HttpContext context);

    /**
     * Returns the handler mapped to a context or null if none is found.
     *
     * @param context context to retrieve
     * @return handler associated with that context
     *
     * @see #getContextHandler(HttpContext)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpHandler getContextHandler(final String context);

    /**
     * Returns the handler mapped to a context or null if it is not found.
     *
     * @param context context to retrieve
     * @return handler associated with that context
     *
     * @see #getContextHandler(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpHandler getContextHandler(final HttpContext context);

    /**
     * Returns a copy of the server's contexts and their respective handlers.
     *
     * @return server's contexts
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Map<HttpContext,HttpHandler> getContexts();

//

    /**
     * Starts the server.
     *
     * @throws IllegalStateException if server is not bound to a valid port
     *
     * @see #stop()
     * @see #stop(int)
     * @since 2.00.00
     * @author Ktt Development
     */
    public abstract void start();

    /**
     * Stops the server and all active requests.
     *
     * @see #start()
     * @see #stop(int)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void stop();

    /**
     * Stops the server with a delay for remaining requests.
     *
     * @param delay delay until server stops all active requests
     *
     * @see #start()
     * @see #stop(int)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void stop(final int delay);

}
