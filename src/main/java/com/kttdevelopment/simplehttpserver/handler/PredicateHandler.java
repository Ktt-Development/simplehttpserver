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
import com.kttdevelopment.simplehttpserver.SimpleHttpExchange;
import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * The handler will process each request differently depending on the predicate.
 *
 * @see SimpleHttpHandler
 * @see HttpHandler
 * @see SimpleHttpExchange
 * @see Predicate
 * @since 01.00.00
 * @version 4.4.0
 * @author Ktt Development
 */
public class PredicateHandler implements HttpHandler {

    private final HttpHandler T, F;
    private final Predicate<HttpExchange> predicate;

    /**
     * Creates a predicate handler.
     *
     * @deprecated use {@link PredicateHandler#PredicateHandler(Predicate, HttpHandler, HttpHandler)}
     * @param trueHandler handler to use if true
     * @param falseHandler handler to use if false
     * @param predicate predicate to test
     *
     * @see SimpleHttpHandler
     * @see HttpHandler
     * @see SimpleHttpExchange
     * @see Predicate
     * @since 01.00.00
     * @author Ktt Development
     */
    @Deprecated
    public PredicateHandler(final HttpHandler trueHandler, final HttpHandler falseHandler, final Predicate<HttpExchange> predicate){
        T = trueHandler;
        F = falseHandler;
        this.predicate = predicate;
    }

    /**
     * Creates a predicate handler.
     *
     * @param predicate predicate to test
     * @param trueHandler handler to use if true
     * @param falseHandler handler to use if false
     *
     * @see SimpleHttpHandler
     * @see HttpHandler
     * @see SimpleHttpExchange
     * @see Predicate
     * @since 4.4.0
     * @author Ktt Development
     */
    public PredicateHandler(final Predicate<HttpExchange> predicate, final HttpHandler trueHandler, final HttpHandler falseHandler){
        this.predicate = predicate;
        T = trueHandler;
        F = falseHandler;
    }

    @Override
    public final void handle(final HttpExchange exchange) throws IOException{
        (predicate.test(exchange) ? T : F).handle(exchange);
    }

//

    @Override
    public String toString(){
        return
            "PredicateHandler"  + '{' +
            "(true) handler"    + '=' +     T.toString()            + ", " +
            "(false) handler"   + '=' +     F.toString()            + ", " +
            "predicate"         + '=' +     predicate.toString()    +
            '}';
    }

}
