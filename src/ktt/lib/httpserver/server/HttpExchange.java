package ktt.lib.httpserver.server;

import com.sun.net.httpserver.Headers;
import ktt.lib.httpserver.http.RequestMethod;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

public abstract class HttpExchange {

    HttpExchange(){ }

//

    static HttpExchange create(com.sun.net.httpserver.HttpExchange exchange){
        return HttpExchangeImpl.createHttpExchange(exchange);
    }

//

    public abstract HttpServer getServer();

    public abstract URI getURI();

    public abstract InetSocketAddress getPublicAddress();

    public abstract InetSocketAddress getLocalAddress();

//

    public abstract HttpContext getHttpContext();

    public abstract HttpPrincipal getHttpPrincipal();

    public abstract String getProtocol();

    @Deprecated
    public abstract String getContext();

    @Deprecated
    public abstract String getScheme();

//

    public abstract Headers getRequestHeaders();

    public abstract RequestMethod getRequestMethod();

//

    public abstract String getRawGet();

    public abstract HashMap<String,String> getGetMap();

    public abstract boolean hasGet();

//

    public abstract String getRawPost();

    @SuppressWarnings("rawtypes")
    public abstract HashMap getPostMap();

    public abstract boolean hasPost();

//

    public abstract Headers getResponseHeaders();

    public abstract int getResponseCode();

//

    public abstract void sendResponseHeaders(final int code, final long length) throws IOException;

    public abstract void send(final int responseCode) throws IOException;

    public abstract void send(final byte[] response) throws IOException;

    public abstract void send(final byte[] response, int responseCode) throws IOException;

    public abstract void send(final String response) throws IOException;

    public abstract void send(final String response, final int responseCode) throws IOException;

//

    public abstract void close();

//

    public abstract Object getAttribute(final String name);

    public abstract void setAttribute(final String name, final Object value);

}