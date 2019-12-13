package ktt.lib.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <i>This class is a simplified implementation of {@link com.sun.net.httpserver.HttpServer}.</i>
 * <br>
 * At least one {@link RequestHandler} must be created in order to process requests. When handling requests the server will use the most specific context, note that contexts are case-sensitive. If no handler can be found the request is rejected with a 404 response.
 * @see com.sun.net.httpserver.HttpServer
 * @see RequestHandler
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings("unused")
public abstract class HttpServer {

    /**
     * Creates a Http Server instance which is initially bounded to a port.
     * @return a Http Server instance
     * @throws IOException if the port is already occupied and can not be bound
     * @since 01.01.01
     */
    public static HttpServer create() throws IOException {
        return HttpServerProvider.createHttpServer();
    }

    /**
     * Creates a Http Server instance which is initially bounded to a port.
     * @param port port to run the server on
     * @return a Http Server instance
     * @throws IOException if the port is already occupied and can not be bound
     * @since 01.00.00
     */
    public static HttpServer create(int port) throws IOException {
        return HttpServerProvider.createHttpServer(port);
    }

    /**
     * Creates a Http Server instance which is initially bounded to a port.
     * @param port port to run the server on
     * @param backlog maximum queued connections
     * @return a Http Server instance
     * @throws IOException if the port is already occupied and can not be bound
     * @since 01.01.00
     */
    public static HttpServer create(int port, int backlog) throws IOException {
        return HttpServerProvider.createHttpServer(port, backlog);
    }

    /**
     * Binds the Http Server to a port.
     * @param port port to bind the server to
     * @throws IOException if the server is already occupied and can not be bound
     * @since 01.00.00
     */
    public abstract void bind(int port) throws IOException;

    /**
     * Binds the Http Server to a port.
     * @param port port to bind the server to
     * @param backlog maximum queued connections
     * @throws IOException if the server is already occupied and can not be bound
     * @since 01.01.00
     */
    public abstract void bind(int port, int backlog) throws IOException;

    /**
     * Returns the server's executor, or null if none exists.
     * @return server executor
     * @see #setExecutor(Executor)
     * @since 01.01.00
     */
    public abstract Executor getExecutor();

    /**
     * Sets the server's executor
     * @param executor server executor
     * @see #getExecutor()
     * @since 01.01.00
     */
    public abstract void setExecutor(Executor executor);

    /**
     * Returns the local address that the server is running on.
     * @return the server's local address
     * @since 01.00.00
     */
    public abstract InetSocketAddress getAddress();

    /**
     * Starts the server.
     * @see #stop()
     * @see #stop(int)
     * @since 01.00.00
     */
    public abstract void start();

    /**
     * Stops all inbound and current requests.
     * @see #start()
     * @see #stop(int)
     * @since 01.00.00
     */
    public abstract void stop();

    /**
     * Stops all inbound requests and closes after all current requests are handled unless it takes longer than the designated delay.
     * @param delay maximum time to close
     * @see #start()
     * @see #stop()
     * @since 01.01.00
     */
    public abstract void stop(int delay);

    /**
     * Creates an empty context.
     * @param path the path
     * @throws IllegalArgumentException if the path is invalid, or is taken
     * @throws NullPointerException if the path is null
     * @see #createContext(String, RequestHandler)
     * @see #removeContext(String)
     * @since 01.01.00
     */
    public abstract void createContext(String path);

    /**
     * Creates a context mapped to a specified {@link RequestHandler}.
     * @param path the path to map the handler to
     * @param handler the request handler
     * @throws IllegalArgumentException if the path is invalid, or is taken
     * @throws NullPointerException if the path is null
     * @see RequestHandler
     * @see #createContext(String)
     * @see #removeContext(String)
     * @since 01.00.00
     */
    public abstract void createContext(String path, RequestHandler handler);

    /**
     * Removes the handler mapped to the context.
     * @param path the path to remove
     * @throws IllegalArgumentException if no handler at the context exists
     * @throws NullPointerException if the path is null
     * @see #createContext(String)
     * @see #createContext(String, RequestHandler)
     * @since 01.00.00
     */
    public abstract void removeContext(String path);

    /**
     * Returns a copy of a map of the server's contexts and their handlers.
     * @return server contexts
     * @since 01.01.00
     */
    public abstract Map<String,RequestHandler> getContexts();

}
