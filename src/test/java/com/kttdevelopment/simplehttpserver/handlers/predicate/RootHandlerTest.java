package com.kttdevelopment.simplehttpserver.handlers.predicate;

import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.RootHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public final class RootHandlerTest {

    @Test
    public final void root() throws IOException, ExecutionException, InterruptedException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final String context = "";
        server.createContext(context,new RootHandler((SimpleHttpHandler) exchange -> exchange.send("A"), (SimpleHttpHandler) exchange -> exchange.send("B")));
        server.start();

        Assertions.assertFalse(server.getContexts().isEmpty(), "Server did not contain a temporary context");

        final String url = "http://localhost:" + port;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assertions.assertEquals("A",response, "Server root response did not match client root response");

        request = HttpRequest.newBuilder()
            .uri(URI.create(url + server.getRandomContext()))
            .build();

        response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assertions.assertEquals("B",response, "Server else response did not match client else response");

        server.stop();
    }

}
