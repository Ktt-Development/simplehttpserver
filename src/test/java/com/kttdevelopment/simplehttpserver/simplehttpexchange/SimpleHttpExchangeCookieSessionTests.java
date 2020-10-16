package com.kttdevelopment.simplehttpserver.simplehttpexchange;

import com.kttdevelopment.simplehttpserver.*;
import com.sun.net.httpserver.HttpContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public final class SimpleHttpExchangeCookieSessionTests {

    @Test
    public final void testSession() throws IOException, ExecutionException, InterruptedException{
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
        final String cookie = "__session-id";
        server.setHttpSessionHandler(new HttpSessionHandler(cookie));
        server.start();

        String url = "http://localhost:" + port + context ;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        final CookieManager cookies = new CookieManager();
        HttpClient client = HttpClient.newBuilder()
            .cookieHandler(cookies)
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        // exchange

        final SimpleHttpExchange exchange = exchangeRef.get();

        Assertions.assertNotNull(server.getHttpSession(exchange), "Client was not assigned a http session");
        final String sessionId = server.getHttpSession(exchange).getSessionID();
        Assertions.assertEquals(sessionId, cookies.getCookieStore().get(URI.create(url)).get(0).getValue(), "Http Session ID did not match cookie session id");

        final HttpSession session = server.getHttpSession(exchange);
        Assertions.assertTrue(session.getCreationTime() < System.currentTimeMillis(), "Client session creation time was in the future");
        Assertions.assertTrue(session.getLastAccessTime() < System.currentTimeMillis(), "Client session last access time was in the future");

        server.stop();
    }

}
