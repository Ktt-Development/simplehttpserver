package com.kttdevelopment.simplehttpserver.simplehttpexchange.io;

import com.kttdevelopment.simplehttpserver.*;
import com.sun.net.httpserver.HttpContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public final class SimpleHttpExchangeGetTest {

    @Test
    public final void get() throws IOException, ExecutionException, InterruptedException{
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
        final AtomicReference<HttpContext> contextRef = new AtomicReference<>();
        contextRef.set(server.createContext(context, handler));
        server.start();

        final String queryKey = "test", queryValue = "value";
        final String altKey   = "alt", altValueRaw = "a+?&}", altValueEnc = "a%2B%3F%26%7D";
        final String url      = "http://localhost:" + port + context + '?' + queryKey + '=' + queryValue + '&' + altKey + '=' + altValueEnc;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assertions.assertEquals(exchangeResponse.get(), response, "Response body did not match response sent");

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assertions.assertEquals(HttpRequestMethod.GET, exchange.getRequestMethod(), "Client request method did not match exchange request method (GET)");
        Assertions.assertTrue(exchange.hasGet(), "Exchange was missing client GET map");
        Assertions.assertEquals(queryValue, exchange.getGetMap().get(queryKey), "Exchange GET did not match client GET");
        Assertions.assertEquals(altValueRaw, exchange.getGetMap().get(altKey), "Exchange GET did not match client GET");
    }

}
