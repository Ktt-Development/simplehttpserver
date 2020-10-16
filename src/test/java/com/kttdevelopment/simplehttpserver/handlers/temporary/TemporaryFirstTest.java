package com.kttdevelopment.simplehttpserver.handlers.temporary;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.TemporaryHandler;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public final class TemporaryFirstTest {

    @Test
    public final void testFirstConn() throws ExecutionException, InterruptedException, IOException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = "";
        server.createContext(context, new TemporaryHandler(server, HttpExchange::close));
        server.start();

        Assertions.assertFalse(server.getContexts().isEmpty(), "Server did not contain a temporary context");

        final String url = "http://localhost:" + port + context;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        Assertions.assertTrue(server.getContexts().isEmpty(), "Server did not remove temporary context");

        server.stop();
    }

}
