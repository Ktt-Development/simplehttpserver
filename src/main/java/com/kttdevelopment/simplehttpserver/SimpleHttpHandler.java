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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * A simplified implementation of {@link HttpHandler}. It is used to process {@link SimpleHttpExchange}s. Each handler is its own thread and will not reveal any exceptions unless a try-catch within the handler is used.
 *
 * @see HttpHandler
 * @see SimpleHttpExchange
 * @since 02.00.00
 * @version 03.03.00
 * @author Ktt Development
 */

public interface SimpleHttpHandler extends HttpHandler {

    /**
     * Encapsulates the {@link #handle(SimpleHttpExchange)} for the authenticator. This method is reserved by the server; <b>do not override this</b>, it will break the {@link #handle(SimpleHttpExchange)} method.
     *
     * @param exchange client information
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    @Override
    default void handle(final HttpExchange exchange) throws IOException{
        handle(SimpleHttpExchange.create(exchange));
    }

    /**
     * Handlers the given request and generates a response <b>if no exceptions occur</b>.
     *
     * @param exchange client information
     * @throws IOException internal failure
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    void handle(final SimpleHttpExchange exchange) throws IOException;

}
