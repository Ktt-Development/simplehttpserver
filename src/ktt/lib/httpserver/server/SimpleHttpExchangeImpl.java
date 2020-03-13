package ktt.lib.httpserver.server;

import com.sun.net.httpserver.*;
import com.sun.net.httpserver.HttpExchange;
import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.http.RequestMethod;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

abstract class SimpleHttpExchangeImpl {

    static SimpleHttpExchange createSimpleHttpExchange(final HttpExchange exchange){
        return new SimpleHttpExchange() {

            private final HttpServer httpServer;
            private final HttpExchange httpExchange;

            private final URI URI;
            private final InetSocketAddress publicAddr, localAddr;

            private final HttpContext httpContext;
            private final HttpPrincipal httpPrincipal;
            private final String protocol, context, scheme;

            private final Headers requestHeaders;
            private final RequestMethod requestMethod;

            private final String rawGet;
            private final HashMap<String,String> getMap;
            private final boolean hasGet;

            private final String rawPost;
            private final HashMap postMap;
            private final boolean hasPost;

            private boolean closed = false;

            {
                httpServer = (httpContext = exchange.getHttpContext()).getServer();
                httpExchange = exchange;
            //
                URI = httpExchange.getRequestURI();
                publicAddr = httpExchange.getRemoteAddress();
                localAddr = httpExchange.getLocalAddress();
            //
                httpPrincipal = httpExchange.getPrincipal();
                protocol = httpExchange.getProtocol();
                context = null; // todo: needs impl
                scheme = null; // todo: needs impl
            //
                requestHeaders = httpExchange.getRequestHeaders();
                switch(exchange.getRequestMethod()){
                    case "GET":
                        requestMethod = RequestMethod.GET; break;
                    case "HEAD":
                        requestMethod = RequestMethod.HEAD; break;
                    case "POST":
                        requestMethod = RequestMethod.POST; break;
                    case "PUT":
                        requestMethod = RequestMethod.PUT; break;
                    case "DELETE":
                        requestMethod = RequestMethod.DELETE; break;
                    case "CONNECT":
                        requestMethod = RequestMethod.CONNECT; break;
                    case "OPTIONS":
                        requestMethod = RequestMethod.OPTIONS; break;
                    case "TRACE":
                        requestMethod = RequestMethod.TRACE; break;
                    case "PATCH":
                        requestMethod = RequestMethod.PATCH; break;
                    default:
                        requestMethod = RequestMethod.UNSUPPORTED; break;
                }
            //
                hasGet = (rawGet = URI.getQuery()) != null;
                getMap = null; // todo: needs parse
            //
                rawPost = null; // todo: needs impl
                postMap = null; // todo: needs impl
                hasPost = false; // todo: needs impl

            }

        //

            @Override
            public final HttpServer getHttpServer(){
                return httpServer;
            }

            @Override
            public final HttpExchange getHttpExchange(){
                return httpExchange;
            }

        //

            @Override
            public final URI getURI(){
                return URI;
            }

            @Override
            public final InetSocketAddress getPublicAddress(){
                return publicAddr;
            }

            @Override
            public final InetSocketAddress getLocalAddress(){
                return localAddr;
            }

        //

            @Override
            public final HttpContext getHttpContext(){
                return httpContext;
            }

            @Override
            public final HttpPrincipal getHttpPrincipal(){
                return httpPrincipal;
            }

            @Override
            public final String getProtocol(){
                return protocol;
            }

            @Override
            public final String getContext(){
                return protocol;
            }

            @Override
            public final String getScheme(){
                return scheme;
            }

        //

            @Override
            public final Headers getRequestHeaders(){
                return requestHeaders;
            }

            @Override
            public final RequestMethod getRequestMethod(){
                return requestMethod;
            }

        //

            @Override
            public final String getRawGet(){
                return rawGet;
            }

            @Override
            public final HashMap<String, String> getGetMap(){
                return getMap;
            }

            @Override
            public final boolean hasGet(){
                return hasGet;
            }

        //

            @Override
            public final String getRawPost(){
                return rawPost;
            }

            @Override @SuppressWarnings("rawtypes")
            public final HashMap getPostMap(){
                return postMap;
            }

            @Override
            public final boolean hasPost(){
                return hasPost;
            }

        //

            @Override
            public final Headers getResponseHeaders(){
                return httpExchange.getResponseHeaders();
            }

            @Override
            public final int getResponseCode(){
                return httpExchange.getResponseCode();
            }

        //

            @Override
            public synchronized final void sendResponseHeaders(final int code, final long length) throws IOException{
                if(closed) return;
                httpExchange.sendResponseHeaders(code, length);
                close();
            }

            @Override
            public synchronized final void send(final int responseCode) throws IOException{
                sendResponseHeaders(responseCode,0);
            }

            @Override
            public synchronized final void send(final byte[] response) throws IOException{
                send(response, HTTPCode.HTTP_OK);
            }

            @Override
            public synchronized final void send(final byte[] response, final int responseCode) throws IOException {
                if(closed) return;

                // todo: needs impl
            }

            @Override
            public synchronized final void send(final String response) throws IOException{
                send(response.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public synchronized final void send(final String response, final int responseCode) throws IOException{
                send(response.getBytes(StandardCharsets.UTF_8),responseCode);
            }

        //

            @Override
            public synchronized final void close(){
                if(!closed){
                    exchange.close();
                    closed = true;
                }
            }

        //

            @Override
            public final Object getAttribute(final String name){
                return httpExchange.getAttribute(name);
            }

            @Override
            public synchronized final void setAttribute(final String name, final Object value){
                httpExchange.setAttribute(name, value);
            }

        };
    }

}
