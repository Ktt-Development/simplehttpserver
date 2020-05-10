package com.kttdevelopment.simplehttpserver;

import com.sun.net.httpserver.*;

import java.io.IOException;

/**
 * <i>This class is a simplified implementation of {@link HttpsServer}.</i><br>
 * At least one {@link HttpHandler} must be created in order to process requests. When handling requests the server will use the most specific context. If no handler can be found it is rejected with a 404 response. <br>
 * <b>Contexts are case-sensitive.</b>
 *
 * @see HttpsServer
 * @see HttpHandler
 * @see SimpleHttpServer
 * @see SimpleHttpHandler
 * @since 02.00.00
 * @version 03.04.00
 * @author Ktt Development
 */
public abstract class SimpleHttpsServer extends SimpleHttpServer {

    /**
    * Create an empty {@link SimpleHttpsServer}. Applications don't use this method.
    *
    * @see SimpleHttpsServerImpl#create(Integer, Integer)
    * @since 02.00.00
    * @author Ktt Development
    */
   SimpleHttpsServer(){ }

//

   /**
    * Creates a {@link SimpleHttpsServer}.
    *
    * @return a {@link SimpleHttpsServer}
    * @throws IOException uncaught exception
    *
    * @since 03.04.00
    * @author Ktt Development
    */
   public static SimpleHttpsServer create() throws IOException {
       return SimpleHttpsServerImpl.create(null,null);
   }

   /**
    * Creates a {@link SimpleHttpsServer} bounded to a port.
    *
    * @param port port to bind to
    * @return a {@link SimpleHttpsServer}
    * @throws java.net.BindException if server can not bind to port
    * @throws NullPointerException if address is <code>null</code>
    * @throws IllegalArgumentException if port is out of range
    * @throws IOException uncaught exception
    *
    * @since 03.04.00
    * @author Ktt Development
    */
   public static SimpleHttpsServer create(final int port) throws IOException {
       return SimpleHttpsServerImpl.create(port,null);
   }

   /**
    * Creates a {@link SimpleHttpsServer} bounded to a port.
    *
    * @param port port to bind to
    * @param backlog request backlog
    * @return a {@link SimpleHttpsServer}
    * @throws java.net.BindException if server can not bind to port
    * @throws NullPointerException if address is <code>null</code>
    * @throws IllegalArgumentException if port is out of range
    * @throws IOException uncaught exception
    *
    * @since 03.04.00
    * @author Ktt Development
    */
   public static SimpleHttpsServer create(final int port, final int backlog) throws IOException {
       return SimpleHttpsServerImpl.create(port,backlog);
   }

//

    /**
     * Returns the native https server.
     *
     * @return https server
     *
     * @see HttpsServer
     * @since 03.04.00
     * @author Ktt Development
     */
    @Override
    public abstract HttpsServer getHttpServer();


//

    /**
     * Sets a https configurator for the server
     *
     * @param config the https configurator
     * @throws NullPointerException if config is null
     *
     * @since 03.04.00
     * @author Ktt Development
     */
    public abstract void setHttpsConfigurator(HttpsConfigurator config);

    /**
     * Returns the https configurator of the server
     *
     * @return the https configurator
     *
     * @since 03.04.00
     * @author Ktt Development
     */
    public abstract HttpsConfigurator getHttpsConfigurator();

}
