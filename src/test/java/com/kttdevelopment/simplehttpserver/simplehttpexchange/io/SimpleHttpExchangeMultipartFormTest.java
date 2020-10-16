package com.kttdevelopment.simplehttpserver.simplehttpexchange.io;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.RequestMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public final class SimpleHttpExchangeMultipartFormTest {

    @SuppressWarnings({"rawtypes", "StringBufferReplaceableByString", "SpellCheckingInspection"})
    @Test
    public final void postMultipartFormData() throws IOException, ExecutionException, InterruptedException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final AtomicReference<SimpleHttpExchange> exchangeRef = new AtomicReference<>();
        final AtomicReference<String> exchangeResponse = new AtomicReference<>();
        final SimpleHttpHandler handler = exchange -> {
            exchangeRef.set(exchange);
            exchangeResponse.set(exchange.toString());
            exchange.send(exchange.toString());
        };

        final String context = "";
        server.createContext(context, handler);
        server.start();

        final String boundary = "d74496d66958873e";

        final String url = "http://localhost:" + port + context ;

        final String key = "key", value = "value";
        final String fkey = "fileKey", filename = "fileName.txt", fvalue = "fileValue", contentType = "text/plain";

        final StringBuilder OUT = new StringBuilder();
        OUT.append("--------------------------").append(boundary).append("\r\n");
        OUT.append("Content-Disposition: ").append("form-data; ").append("name=\"").append(key).append('\"').append("\r\n\r\n");
        OUT.append(value).append("\r\n");
        OUT.append("--------------------------").append(boundary).append("\r\n");
        OUT.append("Content-Disposition: ").append("form-data; ").append("name=\"").append(fkey).append("\"; ");
        OUT.append("filename=\"").append(filename).append('\"').append("\r\n");
        OUT.append("Content-Type: ").append(contentType).append("\r\n\r\n");
        OUT.append(fvalue).append("\r\n");
        OUT.append("--------------------------").append(boundary).append("--");

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-type","multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofString(OUT.toString()))
            .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assertions.assertEquals(RequestMethod.POST, exchange.getRequestMethod(), "Client request method did not match exchange request method (POST)");
        Assertions.assertTrue(exchange.hasPost(), "Exchange was missing client POST map");

        Assertions.assertEquals(value, ((Map) exchange.getPostMap().get(key)).get("value"), "Client form value did not match server value");

        Assertions.assertEquals(filename, ((Map) ((Map) ((Map) ((Map) exchange.getPostMap().get(fkey)).get("headers")).get("Content-Disposition")).get("parameters")).get("filename"), "Client file name did not match server value");
        Assertions.assertEquals(contentType, ((Map) ((Map) ((Map) exchange.getPostMap().get(fkey)).get("headers")).get("Content-Type")).get("header-value"), "Client content-type did not match server value");
        Assertions.assertEquals(fvalue, ((Map) exchange.getPostMap().get(fkey)).get("value"), "Client file value did not match server value");

        // multipart/form-data schema

        Assertions.assertEquals(value, exchange.getMultipartFormData().getRecord(key).getValue(), "Client form value did not match server value");

        Assertions.assertEquals(filename, ((FileRecord) exchange.getMultipartFormData().getRecord(fkey)).getFileName(), "Client file name did not match server value");
        Assertions.assertEquals(contentType, ((FileRecord) exchange.getMultipartFormData().getRecord(fkey)).getContentType(), "Client content-type did not match server value");
        Assertions.assertEquals(fvalue, new String(((FileRecord) exchange.getMultipartFormData().getRecord(fkey)).getBytes()), "Client file value did not match server value");

        server.stop();
    }

}
