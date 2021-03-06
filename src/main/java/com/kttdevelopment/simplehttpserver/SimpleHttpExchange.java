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

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

/**
 * <i>This class is a simplified implementation of {@link HttpExchange}</i>. <br>
 * This class provides methods to process requests from the client and send a response back. The handler will keep executing until a response is sent.<br><br>
 * Get/post type requests for <code>application/x-www-form-urlencoded</code> (default) will be a simple key/value map. <br>
 * Get/post type requests for <code>multipart/form-data</code> will use a {@link MultipartFormData}. <br>
 * If a file is submitted the value will be in bytes unless it is plain text.
 *
 * @see HttpExchange
 * @since 02.00.00
 * @version 4.0.0
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class SimpleHttpExchange {

    /**
     * Create an empty {@link SimpleHttpExchange}. Applications don't use this method.
     *
     * @see SimpleHttpExchangeImpl#create(HttpExchange)
     * @since 02.00.00
     * @author Ktt Development
     */
    protected SimpleHttpExchange() { }

//

    /**
     * Encapsulates a {@link HttpExchange} to create a simpler version. Applications do not use this method.
     *
     * @param exchange native http exchange
     * @return a simplified http exchange
     *
     * @see HttpExchange
     * @since 02.00.00
     * @author Ktt Development
     */
    static SimpleHttpExchange create(final HttpExchange exchange){
        return SimpleHttpExchangeImpl.create(exchange);
    }

//

    /**
     * Returns the server where the request came from.
     *
     * @return server associated with the exchange
     *
     * @see HttpServer
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpServer getHttpServer();

    /**
     * Returns the native http exchange.
     *
     * @return native http exchange
     *
     * @see HttpExchange
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpExchange getHttpExchange();

//

    /**
     * Returns the exchange URI.
     *
     * @return exchange URI
     *
     * @see URI
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract URI getURI();

    /**
     * Returns the client's public address.
     *
     * @return client's public address
     *
     * @see InetSocketAddress
     * @see #getLocalAddress()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract InetSocketAddress getPublicAddress();

    /**
     * Returns the client's local address on the server.
     *
     * @return client's local address
     *
     * @see InetSocketAddress
     * @see #getPublicAddress()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract InetSocketAddress getLocalAddress();

//

    /**
     * Returns the context associated with the exchange.
     *
     * @return associated context
     *
     * @see HttpContext
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpContext getHttpContext();

    /**
     * Returns the http principle for the exchange.
     *
     * @return http principal
     *
     * @see HttpPrincipal
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract HttpPrincipal getHttpPrincipal();

    /**
     * Returns the protocol for the URI address.
     *
     * @return url protocol
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract String getProtocol();

//

    /**
     * Returns the request headers.
     *
     * @return request headers
     *
     * @see Headers
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Headers getRequestHeaders();

    /**
     * Returns the request method.
     *
     * @return request method
     *
     * @see HttpRequestMethod
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract String getRequestMethod();

//

    /**
     * Returns the GET request as a string.
     *
     * @return GET request as a string
     *
     * @see #getGetMap()
     * @see #hasGet()
     * @see URI#getQuery()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract String getRawGet();

    /**
     * Returns the GET request as a map with its keys and values.
     *
     * @return GET request as a map
     *
     * @see #getRawGet()
     * @see #hasGet()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Map<String,String> getGetMap();

    /**
     * Returns if there is a GET request.
     *
     * @return if GET request exists
     *
     * @see #getRawGet()
     * @see #getGetMap()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract boolean hasGet();

//

    /**
     * Returns the POST request as a string.
     *
     * @return POST request as a string
     *
     * @see HttpExchange#getRequestBody()
     * @see #getPostMap()
     * @see #getMultipartFormData()
     * @see #hasPost()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract String getRawPost();

    /**
     * Returns the POST request as keys mapped to values.
     *
     * @return POST request as a map
     *
     * @see #getRawPost()
     * @see #getMultipartFormData()
     * @see #hasPost()
     * @since 02.00.00
     * @author Ktt Development
     */
    @SuppressWarnings("rawtypes")
    public abstract Map getPostMap();

    /**
     * Returns a multipart/form-data as an object or null if there is none.
     *
     * @return POST request as a multipart/form-data
     *
     * @see MultipartFormData
     * @see #getRawPost()
     * @see #getPostMap()
     * @see #hasPost()
     * @since 4.0.0
     * @author Ktt Development
     */
    public abstract MultipartFormData getMultipartFormData();

    /**
     * Returns if there is a POST request.
     *
     * @return if POST request exists
     *
     * @see #getRawPost()
     * @see #getPostMap()
     * @see #getMultipartFormData()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract boolean hasPost();

//

    /**
     * Returns the response headers to be sent.
     *
     * @return response headers
     *
     * @see Headers
     * @see #sendResponseHeaders(int, long)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Headers getResponseHeaders();

    /**
     * Returns the response headers to be sent.
     *
     * @return response code
     *
     * @see java.net.HttpURLConnection
     * @see #sendResponseHeaders(int, long)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract int getResponseCode();

//

    /**
     * Returns the client's existing cookies as a map of keys and values.
     *
     * @return client's cookies
     *
     * @see #setCookie(String, String)
     * @see #setCookie(SimpleHttpCookie)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Map<String,String> getCookies();

    /**
     * Sets a cookie in the response header.
     *
     * @param key name of cookie to set
     * @param value value of cookie
     * @throws IllegalArgumentException if the cookie name is reserved by the server
     *
     * @see SimpleHttpCookie
     * @see #setCookie(SimpleHttpCookie)
     * @see #getCookies()
     * @see #getResponseHeaders()
     * @since 02.03.00
     * @author Ktt Development
     */
    public abstract void setCookie(final String key, final String value);

    /**
     * Sets a cookie in the response header.
     *
     * @param cookie cookie to set
     * @throws IllegalArgumentException if the cookie name is reserved by the server
     *
     * @see SimpleHttpCookie
     * @see #setCookie(String, String)
     * @see #getCookies()
     * @see #getResponseHeaders()
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void setCookie(final SimpleHttpCookie cookie);

//

    /**
     * Returns the exchange response stream.
     * @return exchange output stream
     *
     * @since 03.01.00
     * @author Ktt Development
     */
    public abstract OutputStream getOutputStream();

    /**
     * Sends response headers to the client.
     *
     * @param code response code
     * @param length the size of the response in bytes
     * @throws IOException internal server error
     *
     * @see #getResponseHeaders()
     * @see java.net.HttpURLConnection
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void sendResponseHeaders(final int code, final long length) throws IOException;

    /**
     * Sends a response code to the client.
     *
     * @param responseCode response code
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final int responseCode) throws IOException;

    /**
     * Sends a response to the client.
     *
     * @param response response in bytes
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final byte[] response) throws IOException;

    /**
     * Sends a response to the client.
     *
     * @param response response in bytes
     * @param gzip if response should be gziped before sending
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final byte[] response, final boolean gzip) throws IOException;

    /**
     * Sends a response with response code to the client.
     *
     * @param response response in bytes
     * @param responseCode response code
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final byte[] response, int responseCode) throws IOException;

    /**
     * Sends a response with response code to the client.
     *
     * @param response response in bytes
     * @param responseCode response code
     * @param gzip if response should be gziped before sending it to the client
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final byte[] response, int responseCode, final boolean gzip) throws IOException;

    /**
     * Sends a response to the client.
     *
     * @param response response
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final String response) throws IOException;

    /**
     * Sends a response to the client.
     *
     * @param response response
     * @param gzip if response should be gziped before sending it to the client
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final String response, final boolean gzip) throws IOException;

    /**
     * Sends a response with response code to the client.
     *
     * @param response response
     * @param responseCode response code
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final String response, final int responseCode) throws IOException;

    /**
     * Sends a response with response code to the client.
     *
     * @param response response
     * @param responseCode response code
     * @param gzip if the response should be gziped before sending it to the client
     * @throws IOException internal server error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void send(final String response, final int responseCode, final boolean gzip) throws IOException;

    /**
     * Sends a file  to the client.
     *
     * @param file file to send
     * @throws IOException internal server error or file read error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 4.0.0
     * @author Ktt Development
     */
    public abstract void send(final File file) throws IOException;

    /**
     * Sends a file to the client.
     *
     * @param file file to send
     * @param gzip if the response should be gziped before sending it to the client
     * @throws IOException internal server error or file read error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, int)
     * @see #send(File, int, boolean)
     * @since 4.0.0
     * @author Ktt Development
     */
    public abstract void send(final File file, final boolean gzip) throws IOException;

    /**
     * Sends a file with response code to the client.
     *
     * @param file file to send
     * @param responseCode response code
     * @throws IOException internal server error or file read error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int, boolean)
     * @since 4.0.0
     * @author Ktt Development
     */
    public abstract void send(final File file, final int responseCode) throws IOException;

    /**
     * Sends a file with response code to the client.
     *
     * @param file file to send
     * @param responseCode response code
     * @param gzip if the response should be gziped before sending it to the client
     * @throws IOException internal server error or file read error
     *
     * @see #sendResponseHeaders(int, long)
     * @see #send(int)
     * @see #send(byte[])
     * @see #send(byte[], boolean)
     * @see #send(byte[], int)
     * @see #send(byte[], int, boolean)
     * @see #send(String)
     * @see #send(String, boolean)
     * @see #send(String, int)
     * @see #send(String, int, boolean)
     * @see #send(File)
     * @see #send(File, boolean)
     * @see #send(File, int)
     * @since 4.0.0
     * @author Ktt Development
     */
    public abstract void send(final File file, final int responseCode, final boolean gzip) throws IOException;

//

    /**
     * Closes the exchange between the client and server.
     *
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void close();

//

    /**
     * Returns attribute for filters use.
     *
     * @param name name
     * @return value
     *
     * @see HttpExchange#getAttribute(String)
     * @see #setAttribute(String, Object)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract Object getAttribute(final String name);

    /**
     * Sets attribute for filter use.
     *
     * @param name name
     * @param value value
     *
     * @see HttpExchange#setAttribute(String, Object)
     * @see #getAttribute(String)
     * @since 02.00.00
     * @author Ktt Development
     */
    public abstract void setAttribute(final String name, final Object value);

}
