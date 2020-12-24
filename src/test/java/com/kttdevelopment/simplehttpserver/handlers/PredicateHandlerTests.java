package com.kttdevelopment.simplehttpserver.handlers;

import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.PredicateHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public final class PredicateHandlerTests {

    @Test
    public void predicate() throws IOException, ExecutionException, InterruptedException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final boolean condition = System.currentTimeMillis() % 2 == 1;

        final String context = "";
        server.createContext(context, new PredicateHandler(exchange -> condition, (SimpleHttpHandler) exchange -> exchange.send("A"), (SimpleHttpHandler) exchange -> exchange.send("B")));
        server.start();

        Assertions.assertFalse(server.getContexts().isEmpty(), "Server did not contain a temporary context");

        final String url = "http://localhost:" + port + context;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assertions.assertEquals(condition ? "A" : "B", response, "Server response did not match client response");

        server.stop();
    }

}
