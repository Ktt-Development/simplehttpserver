package com.kttdevelopment.simplehttpserver.handlers;

import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.PredicateHandler;
import org.junit.Assert;
import org.junit.Test;

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
        server.createContext(context,new PredicateHandler((SimpleHttpHandler) exchange -> exchange.send("A"), (SimpleHttpHandler) exchange -> exchange.send("B"), exchange -> condition));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        final String url = "http://localhost:" + port + context;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Server response did not match client response", condition ? "A" : "B", response);

        server.stop();
    }

}
