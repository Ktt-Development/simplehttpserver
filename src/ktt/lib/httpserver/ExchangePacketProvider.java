package ktt.lib.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import ktt.lib.httpserver.http.HTTPCode;
import ktt.lib.httpserver.http.RequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

/**
 * Service provider class for {@link ExchangePacket}. Applications do not normally use this class.
 * @see ExchangePacket
 * @since 01.00.00
 * @version 01.01.01
 * @author Ktt Development
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public abstract class ExchangePacketProvider {

    /**
     * Creates a Exchange Packet using this provider.
     * @param exchange the native Http Exchange
     * @param server the server associated with the request
     * @param context the context associated with the request
     * @return an Exchange Packet instance
     * @see HttpExchange
     * @see HttpServer
     * @since 01.00.00
     */
    static ExchangePacket createExchangePacket(HttpExchange exchange, HttpServer server, String context){
        String handlerContext;
        String requestContext;
        String relativeContext;
        String scheme;

        InetSocketAddress publicAddr, localAddr;

        // Get
        URI uri;
        String rawGet;
        HashMap<String,String> getMap;
        boolean hasGet;

        // Post
        String rawPost;
        HashMap postMap;
        boolean hasPost;

        // Request
        Headers requestHeaders;
        RequestMethod requestMethod;

        // Response
        Headers responseHeaders;
        int responseCode;


        uri = exchange.getRequestURI();
        requestHeaders = exchange.getRequestHeaders();

        /* Source information */ {
            handlerContext = __.startSlash(context);
            requestContext = __.startSlash(uri.getPath());
            relativeContext = __.startSlash(requestContext.substring(handlerContext.length()));
            scheme = uri.getScheme();
            publicAddr = exchange.getRemoteAddress();
            localAddr = exchange.getLocalAddress();
        }

        /* Simple Parse */
        final Function<String,HashMap<String,String>> parseWwwEnc = s -> {
            final HashMap<String,String> out = new HashMap<>();

            final String[] pairs = s.split("&");
            for(String pair : pairs){
                if(pair.contains("=")) {
                    final String[] kv = pair.split("=");
                    out.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), kv.length == 2 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : null);
                }
            }

            return out;
        };

        /* Get */ {
            hasGet = uri.getQuery() != null;
            rawGet =
                    hasGet
                ?
                    uri.getQuery()
                :
                    ""
            ;
            getMap = parseWwwEnc.apply(rawGet);
        }

        /* Post */ {
            String out;
            InputStream IN = exchange.getRequestBody();
            try (Scanner scanner = new Scanner(IN, StandardCharsets.UTF_8.name())) {
                out = scanner.useDelimiter("\\A").next();
                IN.close();
            } catch (NoSuchElementException | IOException ignored) {
                out = "";
            }

            rawPost = out;
            hasPost = !out.isEmpty();

            final String content_type = requestHeaders.getFirst("Content-type");
            if(content_type != null && content_type.startsWith("multipart/form-data")){
                final Pattern hpat = Pattern.compile("(.*): (.*?)(?:$|; )(.*)");
                final Pattern vpat = Pattern.compile("(.*?)=\"(.*?)\"(?:; |$)");

                final String webkitBoundary = content_type.substring(content_type.indexOf("; boundary=") + 11);
                final String startBoundary = "--" + webkitBoundary + "\r\n";
                final String endBoundary = "--" + webkitBoundary + "--\r\n";

                postMap = new HashMap<>();
                /* Parse multipart/form-data */ {
                    final String[] pairs = out.replace(endBoundary,"").split(Pattern.quote(startBoundary));
                    for (String pair : pairs) {
                        final HashMap<String, HashMap> postHeaders = new HashMap<>();
                        if (pair.contains("\r\n\r\n")) {
                            final String[] headers = pair.substring(0, pair.indexOf("\r\n\r\n")).split("\r\n");

                            for (String header : headers) {
                                final HashMap hmap = new HashMap<>();
                                final HashMap<String, String> val = new HashMap<>();

                                final Matcher hmatch = hpat.matcher(header);
                                if (hmatch.find()) {
                                    final Matcher vmatch = vpat.matcher(hmatch.group(3));
                                    while (vmatch.find()) {
                                        val.put(vmatch.group(1), vmatch.group(2));
                                    }
                                    hmap.put("header-name", hmatch.group(1));
                                    hmap.put("header-value", hmatch.group(2));
                                    hmap.put("parameter", val);
                                }
                                postHeaders.put((String) hmap.get("header-name"), hmap);
                            }

                            final HashMap row = new HashMap();
                            row.put("headers",postHeaders);
                            row.put("value",pair.substring(pair.indexOf("\r\n\r\n")+4,pair.lastIndexOf("\r\n")));

                            postMap.put(
                                ((HashMap<String, String>) postHeaders.get("Content-Disposition").get("parameter")).get("name"),
                                row
                            );

                        }
                    }
                }
            }else{
                postMap = parseWwwEnc.apply(rawPost);
            }
        }

        /* Request */ {
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
                    requestMethod = RequestMethod.UNSUPPORTED;
            }
        }

        /* Response */ {
            responseHeaders = exchange.getResponseHeaders();
            responseCode = exchange.getResponseCode();
        }

        return new ExchangePacket() {

            private boolean closed = false;

            @Override
            public final HttpExchange getRawHttpExchange() { return exchange; }

            @Override
            public final HttpServer getServer() { return server; }

            @Override
            public final String getHandlerContext() { return handlerContext; }

            @Override
            public final String getRequestContext() { return requestContext; }

            @Override
            public final String getRelativeContext() { return relativeContext; }

            @Override
            public final String getScheme() { return scheme; }

            @Override
            public final InetSocketAddress getRequestAddress() { return publicAddr; }

            @Override
            public InetSocketAddress getPublicAddress() {
                return publicAddr;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return localAddr;
            }

            @Override
            public final URI getURI() { return uri; }

            @Override
            public final String getRawGet() { return rawGet; }

            @Override
            public final HashMap<String, String> getGetMap() { return getMap; }

            @Override
            public final boolean hasGet() { return hasGet; }

            @Override
            public final String getRawPost() { return rawPost; }

            @Override
            public final HashMap getPostMap() { return postMap; }

            @Override
            public final boolean hasPost() { return hasPost; }

            @Override
            public final Headers getRequestHeaders() { return requestHeaders; }

            @Override
            public final RequestMethod getRequestMethod() { return requestMethod; }

            @Override
            public final Headers getResponseHeaders() { return responseHeaders; }

            @Override
            public final int getResponseCode() { return responseCode; }

            @Override
            public final void sendResponseHeaders(int code, long length) throws IOException {
                if(closed) return;

                exchange.sendResponseHeaders(code,length);
            }

            @Override
            public void send(int code) throws IOException {
                if(closed) return;

                exchange.sendResponseHeaders(code,0);

                close();
            }

            @Override
            public final void send(byte[] response) throws IOException {
                if(closed) return;

                send(response, HTTPCode.HTTP_OK);

                close();
            }

            @Override
            public final void send(byte[] response, int code) throws IOException {
                if(closed) return;

                exchange.getResponseHeaders().set("Accept-Encoding","gzip");
                exchange.getResponseHeaders().set("Content-Encoding","gzip");
                exchange.getResponseHeaders().set("Connection","keep-alive");
                sendResponseHeaders(code, 0);
                GZIPOutputStream OUT = new GZIPOutputStream(exchange.getResponseBody());
                OUT.write(response);
                OUT.finish();
                OUT.close();
                exchange.getResponseBody().close();

                close();
            }

            @Override
            public void send(String response) throws IOException {
                if(closed) return;

                send(response.getBytes(StandardCharsets.UTF_8), HTTPCode.HTTP_OK);
            }

            @Override
            public void send(String response, int code) throws IOException {
                if(closed) return;

                send(response.getBytes(StandardCharsets.UTF_8),code);
            }

            @Override
            public final void close() {
                exchange.close();
                closed = true;
            }
        };
    }

}
