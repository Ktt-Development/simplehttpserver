package ktt.lib.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Service provider class for {@link HttpServer}. Applications do not normally use this class.
 * @see HttpServer
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings("unused")
@Deprecated
abstract class HttpServerProvider {

    /**
     * Create a Http Server instance using this provider.
     * @throws IOException if the port is already occupied or invalid
     * @return a Http Server instance
     * @since 01.01.01
     */
    static HttpServer createHttpServer() throws IOException {
        return new HttpServer() {
            private final Map<String,RequestHandler> contexts = new HashMap<>();
            private boolean running = false;
            private final com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            @Override
            public void bind(int port) throws IOException {
                bind(port,0);
            }

            @Override
            public void bind(int port, int backlog) throws IOException {
                server.bind(new InetSocketAddress(port),backlog);
            }

            @Override
            public Executor getExecutor() {
                return server.getExecutor();
            }

            @Override
            public void setExecutor(Executor executor) {
                server.setExecutor(executor);
            }

            @Override
            public final InetSocketAddress getAddress() {
                return server.getAddress();
            }

            @Override
            public final void start() {
                if(!running){
                    server.start();
                    running = true;
                }
            }

            @Override
            public final void stop() {
                stop(0);
            }

            @Override
            public void stop(int delay) {
                if(running){
                    server.stop(delay);
                    running = false;
                }
            }

            @Override
            public void createContext(String path) {
                path = __.startSlash(path);
                server.createContext(path);
                contexts.put(path,null);
            }

            @Override
            public final void createContext(String path, RequestHandler handler) {
                path = __.startSlash(path);
                final String fpath = path;
                server.createContext(path, httpExchange -> handler._consume(ExchangePacketProvider.createExchangePacket(httpExchange, this, fpath)));
                contexts.put(path,handler);
            }

            @Override
            public void removeContext(String path) {
                path = __.startSlash(path);
                server.removeContext(path);
                contexts.remove(path);
            }

            @Override
            public Map<String, RequestHandler> getContexts() {
                return new HashMap<>(contexts);
            }
        };
    }

    /**
     * Creates a Http Server instance using this provider.
     * @param port port to run the server on
     * @return a Http Server instance
     * @throws IOException if the port is already occupied and can not be bound
     * @since 01.00.00
     */
    static HttpServer createHttpServer(int port) throws IOException {
        final HttpServer server = createHttpServer();
        server.bind(port);
        return server;
    }

    /**
     * Creates a Http Server instance using this provider.
     * @param port port to run the server on
     * @param backlog maximum queued connections
     * @return a Http Server instance
     * @throws IOException if the port is already occupied and can not be bound
     * @since 01.01.00
     */
    static HttpServer createHttpServer(int port, int backlog) throws IOException {
        final HttpServer server = createHttpServer();
        server.bind(port, backlog);
        return server;
    }

}
