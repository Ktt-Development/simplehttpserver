package com.kttdevelopment.simplehttpserver.simplehttpexchange;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import com.sun.net.httpserver.HttpContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public final class SimpleHttpExchangeReadTests {

    @Test
    public final void read() throws IOException, InterruptedException, ExecutionException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final AtomicReference<SimpleHttpExchange> exchangeRef = new AtomicReference<>();
        final AtomicReference<String> exchangeResponse = new AtomicReference<>();
        final SimpleHttpHandler handler = exchange -> {
            exchangeRef.set(exchange);
            exchangeResponse.set(exchange.toString());
            exchange.send(exchange.toString());
        };

        final String context = "/";
        final AtomicReference<HttpContext> contextRef = new AtomicReference<>();
        contextRef.set(server.createContext(context, handler));
        server.start();

        String url = "http://localhost:" + port + context;

        final String headerKey = "header_key", headerValue = "header_value";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header(headerKey, headerValue)
            .build();

        String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assertions.assertEquals(response, exchangeResponse.get(), "Response body did not match response sent");

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assertions.assertEquals(server.getHttpServer(), exchange.getHttpServer(), "Server from exchange was not equal to server issuing request");
        Assertions.assertNotNull(exchange.getHttpExchange(), "Native http exchange should not be null");

        Assertions.assertEquals(context, exchange.getURI().getPath(), "Request context was not equal to handler context");

        Assertions.assertNotNull(exchange.getPublicAddress(), "Client issuing request should not be null");
        Assertions.assertNotNull(exchange.getLocalAddress(), "Local address should be local address");

        Assertions.assertEquals(contextRef.get(), exchange.getHttpContext(), "HttpContext from handler should be same as request");

        Assertions.assertEquals("HTTP/1.1", exchange.getProtocol(), "HttpProtocol should've been HTTP/1.1 for this request");

        Assertions.assertEquals(headerValue, exchange.getRequestHeaders().getFirst(headerKey), "Client header did not match exchange header");

        Assertions.assertFalse(exchange.hasGet(), "Client did not send a GET request");
        Assertions.assertFalse(exchange.hasPost(), "Client did not send a POST request");

        Assertions.assertEquals(HttpCode.HTTP_OK, exchange.getResponseCode(), "Successful exchange should've sent 200 HTTP OK");

        Assertions.assertTrue(exchange.getCookies().isEmpty(), "Client did not send any cookies");

        server.stop();
    }

}
