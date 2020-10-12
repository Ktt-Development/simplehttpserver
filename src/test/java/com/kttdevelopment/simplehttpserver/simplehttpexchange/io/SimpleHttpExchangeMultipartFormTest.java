package com.kttdevelopment.simplehttpserver.simplehttpexchange.io;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.RequestMethod;
import org.junit.Assert;
import org.junit.Test;

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
        server.createContext(context,handler);
        server.start();

        final String boundary = "d74496d66958873e";

        final String url = "http://localhost:" + port + context ;

        final String key = "key", value = "value";
        final String fkey = "fileKey", filename = "fileName.txt", fvalue = "fileValue";

        final StringBuilder OUT = new StringBuilder();
        OUT.append("--------------------------").append(boundary).append("\r\n");
        OUT.append("Content-Disposition: ").append("form-data; ").append("name=\"").append(key).append('\"').append("\r\n\r\n");
        OUT.append(value).append("\r\n");
        OUT.append("--------------------------").append(boundary).append("\r\n");
        OUT.append("Content-Disposition: ").append("form-data; ").append("name=\"").append(fkey).append("\"; ");
        OUT.append("filename=\"").append(filename).append('\"').append('\n');
        OUT.append("Content-Type: ").append("text/plain").append("\r\n\r\n");
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

        Assert.assertEquals("Client request method did not match exchange request method (POST)", RequestMethod.POST,exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client POST map", exchange.hasPost());

        Assert.assertEquals("Client form value did not match server value",value, ((Map) exchange.getPostMap().get(key)).get("value"));
        Assert.assertEquals("Client file name did not match server value",filename, ((Map) ((Map) ((Map) ((Map) exchange.getPostMap().get(fkey)).get("headers")).get("Content-Disposition")).get("parameters")).get("filename"));
        Assert.assertEquals("Client file value did not match server value",fvalue, ((Map) exchange.getPostMap().get(fkey)).get("value"));

        server.stop();
    }

}
