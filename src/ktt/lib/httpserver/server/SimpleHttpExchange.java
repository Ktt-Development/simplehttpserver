package ktt.lib.httpserver.server;

import com.sun.net.httpserver.*;
import com.sun.net.httpserver.HttpExchange;
import ktt.lib.httpserver.http.RequestMethod;
import ktt.lib.httpserver.http.SimpleHTTPCookie;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

public abstract class SimpleHttpExchange {

    SimpleHttpExchange() { }

//

    static SimpleHttpExchange create(final HttpExchange exchange){
        return SimpleHttpExchangeImpl.createSimpleHttpExchange(exchange);
    }

//

    public abstract HttpServer getHttpServer();

    public abstract HttpExchange getHttpExchange();

//

    public abstract URI getURI();

    public abstract InetSocketAddress getPublicAddress();

    public abstract InetSocketAddress getLocalAddress();

//

    public abstract HttpContext getHttpContext();

    public abstract HttpPrincipal getHttpPrincipal();

    public abstract String getProtocol();

    public abstract String getScheme();

    public abstract String getAuthority();

    @Deprecated
    public abstract String getQuery();

    public abstract String getContext();

    public abstract String getFragment();

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

    public abstract HashMap<String,String> getCookies();

    public abstract void setCookie(final SimpleHTTPCookie cookie);

//

    public abstract void sendResponseHeaders(final int code, final long length) throws IOException;

    public abstract void send(final int responseCode) throws IOException;

    public abstract void send(final byte[] response) throws IOException;

    public abstract void send(final byte[] response, final boolean gzip) throws IOException;

    public abstract void send(final byte[] response, int responseCode) throws IOException;

    public abstract void send(final byte[] response, int responseCode, final boolean gzip) throws IOException;

    public abstract void send(final String response) throws IOException;

    public abstract void send(final String response, final boolean gzip) throws IOException;

    public abstract void send(final String response, final int responseCode) throws IOException;

    public abstract void send(final String response, final int responseCode, final boolean gzip) throws IOException;

//

    public abstract void close();

//

    public abstract Object getAttribute(final String name);

    public abstract void setAttribute(final String name, final Object value);

}
