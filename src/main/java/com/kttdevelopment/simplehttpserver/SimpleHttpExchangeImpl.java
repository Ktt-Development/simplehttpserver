package com.kttdevelopment.simplehttpserver;

import com.sun.net.httpserver.*;
import com.kttdevelopment.simplehttpserver.var.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

/**
 * Implementation for {@link SimpleHttpExchange}. Applications do not use this class.
 *
 * @see SimpleHttpExchange
 * @since 02.00.00
 * @version 4.0.0
 * @author Ktt Development
 */
@SuppressWarnings("SpellCheckingInspection")
final class SimpleHttpExchangeImpl extends SimpleHttpExchange {

    private final HttpServer httpServer;
    private final HttpExchange httpExchange;

    private final URI URI;
    private final InetSocketAddress publicAddr, localAddr;

    private final HttpContext httpContext;
    private final HttpPrincipal httpPrincipal;
    private final String protocol;

    private final Headers requestHeaders;
    private final RequestMethod requestMethod;

    private final String rawGet;
    private final Map<String, String> getMap;
    private final boolean hasGet;

    private final String rawPost;
    @SuppressWarnings("rawtypes")
    private final Map postMap;
    private final boolean hasPost;

    private final Map<String, String> cookies;

    private final OutputStream outputStream;

    @SuppressWarnings("FieldCanBeLocal")
    private final Function<String, Map<String, String>> parseWwwFormEnc = s -> {
        final LinkedHashMap<String, String> OUT = new LinkedHashMap<>();
        final String[] pairs = s.split("&");

        for(final String pair : pairs){
            if(pair.contains("=")){
                final String[] kv = pair.split("=");
                OUT.put(
                    URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                    kv.length == 2 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : null
                );
            }
        }
        return OUT;
    };

    /**
     * Creates a {@link SimpleHttpExchange}.
     *
     * @param exchange native {@link HttpExchange}
     * @return an {@link SimpleHttpExchange}
     *
     * @see SimpleHttpExchange
     * @see HttpExchange
     * @since 03.04.00
     * @author Ktt Development
     */
    static SimpleHttpExchange create(final HttpExchange exchange){
        return new SimpleHttpExchangeImpl(exchange);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    SimpleHttpExchangeImpl(final HttpExchange exchange){
        httpServer = (httpContext = exchange.getHttpContext()).getServer();
        httpExchange = exchange;
    //
        URI = httpExchange.getRequestURI();
        publicAddr  = httpExchange.getRemoteAddress();
        localAddr   = httpExchange.getLocalAddress();
    //
        httpPrincipal = httpExchange.getPrincipal();
        protocol    = httpExchange.getProtocol();
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
        getMap = hasGet ? parseWwwFormEnc.apply(rawGet) : new HashMap<>();

    //
        String OUT;

        try(InputStream IN = httpExchange.getRequestBody(); final Scanner scanner = new Scanner(IN, StandardCharsets.UTF_8)){
            OUT = scanner.useDelimiter("\\A").next();
        }catch(final IOException | NoSuchElementException ignored){
            OUT = null;
        }

        hasPost = (rawPost = OUT) != null;

        if(hasPost){
            final String content_type = requestHeaders.getFirst("Content-type");
            if(content_type != null && content_type.startsWith("multipart/form-data")){
                final Pattern boundaryHeaderPattern = Pattern.compile("(.*): (.*?)(?:$|; )(.*)"); // returns the headers listed after each webkit boundary
                final Pattern contentDispositionKVPPattern = Pattern.compile("(.*?)=\"(.*?)\"(?:; |$)"); // returns the keys, values, and parameters for the content disposition header

                final String webkitBoundary = content_type.substring(content_type.indexOf("; boundary=") + 11);
                final String startBoundary = "--" + webkitBoundary + "\r\n";
                final String endBoundary = "--" + webkitBoundary + "--\r\n"; // the final boundary in the request

                postMap = new HashMap<>();
                final String[] pairs = OUT.replace(endBoundary,"").split(Pattern.quote(startBoundary));
                for(String pair : pairs){
                    final Map<String, Map> postHeaders = new HashMap<>();
                    if(pair.contains("\r\n\r\n")){
                        final String[] headers = pair.substring(0, pair.indexOf("\r\n\r\n")).split("\r\n");

                        for (String header : headers) {
                            final Map headerMap = new HashMap<>();
                            final Map<String, String> val = new HashMap<>();

                            final Matcher headerMatcher = boundaryHeaderPattern.matcher(header);
                            if (headerMatcher.find()) {
                                final Matcher contentDispositionKVPMatcher = contentDispositionKVPPattern.matcher(headerMatcher.group(3));
                                while (contentDispositionKVPMatcher.find())
                                    val.put(contentDispositionKVPMatcher.group(1), contentDispositionKVPMatcher.group(2));

                                headerMap.put("header-name", headerMatcher.group(1));
                                headerMap.put("header-value", headerMatcher.group(2));
                                headerMap.put("parameters", val);
                            }
                            postHeaders.put((String) headerMap.get("header-name"), headerMap);
                        }

                        final Map row = new HashMap();
                        row.put("headers", postHeaders);
                        row.put("value", pair.substring(pair.indexOf("\r\n\r\n")+4, pair.lastIndexOf("\r\n")));

                        postMap.put(
                            ((HashMap<String, String>) postHeaders.get("Content-Disposition").get("parameters")).get("name"),
                            row
                        );
                    }
                }
            }else{
                postMap = parseWwwFormEnc.apply(rawPost);
            }
        }else{
            postMap = new HashMap();
        }

        final String rawCookie = requestHeaders.getFirst("Cookie");
        cookies = new HashMap<>();
        if(rawCookie != null && !rawCookie.isEmpty()){
            final String[] cookedCookie = rawCookie.split("; "); // pair
            for(final String pair : cookedCookie){
                String[] value = pair.split("=");
                cookies.put(value[0], value[1]);
            }
        }

        outputStream = exchange.getResponseBody();
    }

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
    public final Map<String, String> getGetMap(){
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
    public final Map getPostMap(){
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
    public final Map<String, String> getCookies(){
        return new HashMap<>(cookies);
    }

    @Override
    public synchronized final void setCookie(final String key, final String value){
        setCookie(new SimpleHttpCookie.Builder(key, value).build());
    }

    @Override
    public synchronized final void setCookie(final SimpleHttpCookie cookie){
        final String cstring = cookie.toCookieHeaderString();
        getResponseHeaders().add("Set-Cookie", cstring);
    }

//

    @Override
    public final OutputStream getOutputStream(){
        return outputStream;
    }

    //

    @Override
    public synchronized final void sendResponseHeaders(final int code, final long length) throws IOException{
        httpExchange.sendResponseHeaders(code, length);
    }

    @Override
    public synchronized final void send(final int responseCode) throws IOException{
        sendResponseHeaders(responseCode, 0);
    }

    @Override
    public synchronized final void send(final byte[] response) throws IOException{
        send(response, HttpCode.HTTP_OK, false);
    }

    @Override
    public final void send(final byte[] response, final boolean gzip) throws IOException{
        send(response, HttpCode.HTTP_OK, gzip);
    }

    @Override
    public synchronized final void send(final byte[] response, final int responseCode) throws IOException {
        send(response, responseCode, false);
    }

    @Override
    public final void send(final byte[] response, final int responseCode, final boolean gzip) throws IOException{
        if(gzip){
            httpExchange.getResponseHeaders().set("Accept-Encoding","gzip");
            httpExchange.getResponseHeaders().set("Content-Encoding","gzip");
            httpExchange.getResponseHeaders().set("Connection","keep-alive");
            sendResponseHeaders(responseCode, 0);
            try(GZIPOutputStream OUT = new GZIPOutputStream(httpExchange.getResponseBody())){
                OUT.write(response);
                OUT.finish();
                OUT.flush();
            }
        }else{
            sendResponseHeaders(responseCode, response.length);
            try(final OutputStream OUT = httpExchange.getResponseBody()){
                OUT.write(response);
                OUT.flush();
            }
        }
    }

    @Override
    public synchronized final void send(final String response) throws IOException{
        send(response.getBytes(StandardCharsets.UTF_8), HttpCode.HTTP_OK, false);
    }

    @Override
    public final void send(final String response, final boolean gzip) throws IOException{
        send(response.getBytes(StandardCharsets.UTF_8), HttpCode.HTTP_OK, gzip);
    }

    @Override
    public synchronized final void send(final String response, final int responseCode) throws IOException{
        send(response.getBytes(StandardCharsets.UTF_8), responseCode, false);
    }

    @Override
    public final void send(final String response, final int responseCode, final boolean gzip) throws IOException{
        send(response.getBytes(StandardCharsets.UTF_8), responseCode, gzip);
    }

    @Override
    public final void send(final File file) throws IOException{
        send(Files.readAllBytes(file.toPath()));
    }

    @Override
    public final void send(final File file, final boolean gzip) throws IOException{
        send(Files.readAllBytes(file.toPath()), true);
    }

    @Override
    public final void send(final File file, final int responseCode) throws IOException{
        send(Files.readAllBytes(file.toPath()), responseCode);
    }

    @Override
    public final void send(final File file, final int responseCode, final boolean gzip) throws IOException{
        send(Files.readAllBytes(file.toPath()), responseCode, gzip);
    }

    //

    @Override
    public synchronized final void close(){
        try{
            outputStream.close();
        }catch(final IOException ignored){ }
        httpExchange.close();
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

//

    @Override
    public String toString(){
        return
            "SimpleHttpExchange"    + '{' +
            "httpServer"            + '=' +     httpServer              + ", " +
            "httpExchange"          + '=' +     httpExchange            + ", " +
            "URI"                   + '=' +     URI                     + ", " +
            "publicAddress"         + '=' +     publicAddr              + ", " +
            "localAddress"          + '=' +     localAddr               + ", " +
            "httpContext"           + '=' +     httpContext             + ", " +
            "httpPrincipal"         + '=' +     httpPrincipal           + ", " +
            "protocol"              + '=' +     protocol                + ", " +
            "requestHeaders"        + '=' +     requestHeaders          + ", " +
            "requestMethod"         + '=' +     requestMethod           + ", " +
            "responseHeaders"       + '=' +     getResponseHeaders()    + ", " +
            "responseCode"          + '=' +     getResponseCode()       + ", " +
            "rawGet"                + '=' +     rawGet                  + ", " +
            "getMap"                + '=' +     getMap                  + ", " +
            "hasGet"                + '=' +     hasGet                  + ", " +
            "rawPost"               + '=' +     rawPost                 + ", " +
            "postMap"               + '=' +     postMap                 + ", " +
            "hasPost"               + '=' +     hasPost                 + ", " +
            "cookies"               + '=' +     cookies                 +
            '}';
    }
}
