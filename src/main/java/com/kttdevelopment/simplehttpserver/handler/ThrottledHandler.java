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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * This handler limits the amount of active connections to a handler. This can be used to limit the amount of simultaneous downloads, or prevent duplicate connections by users.
 *
 * @see ExchangeThrottler
 * @see ServerExchangeThrottler
 * @see SessionThrottler
 * @see ServerSessionThrottler
 * @since 03.03.00
 * @version 03.05.07
 * @author Ktt Development
 */
public class ThrottledHandler implements HttpHandler {

    private final HttpHandler     handler;
    private final ConnectionThrottler throttler;

    /**
     * Creates a throttled handler using a throttler.
     *
     * @param handler handler to use
     * @param throttler how to throttle connections
     *
     * @see HttpHandler
     * @see ExchangeThrottler
     * @see ServerExchangeThrottler
     * @see SessionThrottler
     * @see ServerSessionThrottler
     * @since 03.03.00
     * @author Ktt Development
     */
    @SuppressWarnings("ClassEscapesDefinedScope") // class is required for throttler to function, scope is locked to this package only; ignore visibility error
    public ThrottledHandler(final HttpHandler handler, final ConnectionThrottler throttler){
        this.handler = handler;
        this.throttler = throttler;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        if(throttler.addConnection(exchange)){
            try{
                handler.handle(exchange);
            }finally{
                throttler.deleteConnection(exchange);
            }
        }else{
            exchange.close();
        }
    }

    @Override
    public String toString(){
        return
            "ThrottledHandler"  + '{' +
            "handler"           + '=' +     handler     + ", " +
            "throttler"         + '=' +     throttler   +
            '}';
    }

}
