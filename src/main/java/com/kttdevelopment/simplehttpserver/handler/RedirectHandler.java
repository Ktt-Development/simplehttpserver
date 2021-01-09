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

package com.kttdevelopment.simplehttpserver.handler;

import com.kttdevelopment.simplehttpserver.SimpleHttpExchange;
import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * A request handler that redirects to a different URL without pushing to the history. The URL may not work correctly if it does not have a valid authority (<code>http</code>/<code>https</code>).
 *
 * @see SimpleHttpHandler
 * @see com.sun.net.httpserver.HttpHandler
 * @since 01.00.00
 * @version 03.05.00
 * @author Ktt Development
 */
public class RedirectHandler implements SimpleHttpHandler {

    private final String link;

    /**
     * Creates a redirect to a URL.
     *
     * @param link URL to redirect to
     *
     * @since 01.00.00
     * @author Ktt Development
     */
    public RedirectHandler(final String link){
        this.link = link;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        SimpleHttpHandler.super.handle(exchange);
    }

    @Override
    public final void handle(final SimpleHttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Location", link);
        exchange.send(HttpURLConnection.HTTP_OK);
        exchange.close();
    }

//

    @Override
    public String toString(){
        return
            "RedirectHandler"   + '{' +
            "link"              + '=' + '\'' + link + '\'' +
            '}';
    }

}
