/*
 * Copyright (C) 2021 Ktt Development
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.simplehttpserver;

import com.sun.net.httpserver.*;

import java.io.IOException;

/**
 * <i>This class is a simplified implementation of {@link HttpsServer}.</i>
 * The server must have a {@link HttpsConfigurator} set using {@link #setHttpsConfigurator(HttpsConfigurator)}.<br>
 * At least one {@link HttpHandler} must be created in order to process requests. When handling requests the server will use the most specific context. If no handler can be found it is rejected with a 404 response. <br>
 * <b>Contexts are case-sensitive.</b>
 *
 * @see HttpsServer
 * @see HttpHandler
 * @see SimpleHttpServer
 * @see SimpleHttpHandler
 * @since 02.00.00
 * @version 03.04.01
 * @author Ktt Development
 */
public abstract class SimpleHttpsServer extends SimpleHttpServer {

    /**
    * Creates an empty {@link SimpleHttpsServer}. Server is created using {@link #create()}, {@link #create(int)}, or {@link #create(int, int)}.
    *
    * @see #create()
    * @see #create(int)
    * @see #create(int, int)
    * @see SimpleHttpsServerImpl#createSimpleHttpsServer(Integer, Integer)
    * @since 02.00.00
    * @author Ktt Development
    */
   protected SimpleHttpsServer(){ }

//

   /**
    * Creates a {@link SimpleHttpsServer}. The server must have a {@link HttpsConfigurator} set using {@link #setHttpsConfigurator(HttpsConfigurator)}.
    *
    * @return a {@link SimpleHttpsServer}
    * @throws IOException uncaught exception
    *
    * @since 03.04.00
    * @author Ktt Development
    */
   public static SimpleHttpsServer create() throws IOException {
       return SimpleHttpsServerImpl.createSimpleHttpsServer(null, null);
   }

   /**
    * Creates a {@link SimpleHttpsServer} bounded to a port. The server must have a {@link HttpsConfigurator} set using {@link #setHttpsConfigurator(HttpsConfigurator)}.
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
       return SimpleHttpsServerImpl.createSimpleHttpsServer(port, null);
   }

   /**
    * Creates a {@link SimpleHttpsServer} bounded to a port. The server must have a {@link HttpsConfigurator} set using {@link #setHttpsConfigurator(HttpsConfigurator)}.
    *
    * @param port port to bind to
    * @param backlog maximum amount of inbound connections allowed
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
       return SimpleHttpsServerImpl.createSimpleHttpsServer(port, backlog);
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
