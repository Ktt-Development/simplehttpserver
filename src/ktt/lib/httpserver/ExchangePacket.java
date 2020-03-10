package ktt.lib.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.http.RequestMethod;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

/**
 * <i>This class is a simplified implementation of {@link com.sun.net.httpserver.HttpExchange}.</i>
 * <br>
 * This class provides methods to examine requests from the client, and send a response back. Each request can only send one response. Responses are gziped by default.
 * <br> <br>
 * Get/post type requests for <code>application/x-www-form-urlencoded</code> (default) will be a simple key/value map. <br>
 * Get/post type requests for <code>multipart/form-data</code> will use an expanded map. <br>
 * Example:
 * <pre>
 *      {
 *          name : {
 *              headers: {
 *                  Content-Disposition : {
 *                      header-name: "Content-Disposition",
 *                      header-value: "form-data",
 *                      parameters: [
 *                          filename: "file.txt",
 *                          name: "file"
 *                      ]
 *                  },
 *                  Content-Type: {
 *                      header-name: "Content-Type",
 *                      header-value: "text/plain",
 *                      parameters: []
 *                  }
 *              },
 *              value: "file content"
 *          }
 *      }
 * </pre>
 * If a file is submitted the value will be in bytes unless it is plain text.
 * @see HttpExchange
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings("unused")
@Deprecated
public abstract class ExchangePacket {

    /**
     * Creates a Exchange Packet using this provider. Applications do not normally use this method.
     * @param exchange the native Http Exchange
     * @param server the server associated with the request
     * @param context the context associated with the request
     * @return an Exchange Packet instance
     * @see HttpExchange
     * @see HttpServer
     * @since 01.00.00
     */
    static ExchangePacket createExchangePacket(HttpExchange exchange, HttpServer server, String context){
        return ExchangePacketProvider.createExchangePacket(exchange,server,context);
    }

    /**
     * Returns the native Http Exchange.
     * @return the native Http Exchange
     * @see HttpExchange
     * @since 01.00.00
     */
    public abstract HttpExchange getRawHttpExchange();

    /**
     * Returns the server in which the request came from.
     * @return the server where the request came from
     * @see HttpServer
     * @since 01.00.00
     */
    public abstract HttpServer getServer();

    /**
     * Returns the context that the handler was created under.
     * @return the handler's context
     * @see #getRequestContext() ()
     * @see #getRelativeContext()
     * @see #getScheme()
     * @since 01.00.00
     */
    public abstract String getHandlerContext();

    /**
     * Returns the context that the client requested.
     * @return the client's context
     * @see #getHandlerContext()
     * @see #getRelativeContext()
     * @see #getScheme()
     * @since 01.00.00
     */
    public abstract String getRequestContext();

    /**
     * Returns the string between the handler context and the request context.
     * @return the relative context
     * @see #getHandlerContext()
     * @see #getRequestContext()
     * @see #getScheme()
     * @since 01.00.00
     */
    public abstract String getRelativeContext();

    /**
     * Returns the scheme of the context (the # in the context).
     * @return the context scheme
     * @see #getHandlerContext()
     * @see #getRequestContext()
     * @see #getRelativeContext()
     * @since 01.00.00
     */
    public abstract String getScheme();

    /**
     * Returns the client's address.
     * @return the client's address
     * @see #getPublicAddress()
     * @see #getLocalAddress()
     * @deprecated use {@link #getPublicAddress()} instead
     * @since 01.00.00
     */
    @Deprecated
    public abstract InetSocketAddress getRequestAddress();

    /**
     * Returns the client's public address.
     * @return the client's address
     * @see #getLocalAddress()
     * @since 01.01.00
     */
    public abstract InetSocketAddress getPublicAddress();

    /**
     * returns the client's local address on the server.
     * @return the client's address
     * @see #getPublicAddress()
     * @since 01.01.00
     */
    public abstract InetSocketAddress getLocalAddress();

    /**
     * Returns the request URI.
     * @return the request URI
     * @since 01.00.00
     */
    public abstract URI getURI();

    /**
     * Returns the GET request as a String.
     * @return GET request as a String
     * @see #getGetMap()
     * @see #hasGet()
     * @since 01.00.00
     */
    public abstract String getRawGet();

    /**
     * Returns the GET request as keys mapped to values.
     * @return GET request as a Map
     * @see #getRawGet()
     * @see #hasGet()
     * @since 01.00.00
     */
    public abstract HashMap<String,String> getGetMap();

    /**
     * Returns if there is a GET request.
     * @return if GET request exists
     * @see #getRawGet()
     * @see #getGetMap()
     * @since 01.00.00
     */
    public abstract boolean hasGet();

    /**
     * Returns the POST request as a String. Note that syntax of the multipart/form-data is not the same as the GET request.
     * @return POST request as a String
     * @see #getPostMap()
     * @see #hasPost()
     * @since 01.00.00
     */
    public abstract String getRawPost();

    /**
     * Returns the POST request as keys mapped to values. Note that files in a multipart/form-data will return bytes.
     * @return POST request as a Map
     * @see #getRawPost()
     * @see #hasPost()
     * @since 01.00.00
     */
    @SuppressWarnings("rawtypes")
    public abstract HashMap getPostMap();

    /**
     * Returns if there is a POST request.
     * @return if POST request exists
     * @see #getRawPost()
     * @see #getPostMap()
     * @since 01.00.00
     */
    public abstract boolean hasPost();

    /**
     * Returns the request headers.
     * @return request headers
     * @since 01.00.00
     */
    public abstract Headers getRequestHeaders();

    /**
     * Returns the request method.
     * @return request method
     * @see RequestMethod
     * @since 01.00.00
     */
    public abstract RequestMethod getRequestMethod();

    /**
     * Returns response headers that will be sent to the client.
     * @return response headers
     * @since 01.00.00
     */
    public abstract Headers getResponseHeaders();

    /**
     * Returns the response code that will be sent to the client.
     * @return response code
     * @see HTTPCode
     * @since 01.00.00
     */
    public abstract int getResponseCode();

    /**
     * Sends response headers to the client.
     * @param code response code
     * @param length the size of the response in bytes
     * @throws IOException internal server error
     * @see HTTPCode
     * @since 01.00.00
     */
    public abstract void sendResponseHeaders(int code, long length) throws IOException;

    /**
     * Sends response to the client with code only.
     * @param code response code
     * @throws IOException internal server error
     * @see HTTPCode
     * @since 01.00.00
     */
    public abstract void send(int code) throws IOException;

    /**
     * Sends response to the client with code 200.
     * @param response bytes to send
     * @throws IOException internal server error
     * @since 01.00.00
     */
    public abstract void send(byte[] response) throws IOException;

    /**
     * Sends response to the client.
     * @param response bytes to send
     * @param code response code
     * @throws IOException internal server error
     * @see HTTPCode
     * @since 01.00.00
     */
    public abstract void send(byte[] response, int code) throws IOException;

    /**
     * Sends response to the client with code 200.
     * @param response String to send
     * @throws IOException internal server error
     * @since 01.00.00
     */
    public abstract void send(String response) throws IOException;

    /**
     * Sends response to the client.
     * @param response String to send
     * @param code response code
     * @throws IOException internal server error
     * @see HTTPCode
     * @since 01.00.00
     */
    public abstract void send(String response, int code) throws IOException;

    /**
     * Closes the request.
     * @since 01.00.00
     */
    public abstract void close();

}
