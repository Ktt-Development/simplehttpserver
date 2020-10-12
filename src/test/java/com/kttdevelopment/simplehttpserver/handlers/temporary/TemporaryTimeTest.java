package com.kttdevelopment.simplehttpserver.handlers.temporary;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.TemporaryHandler;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public final class TemporaryTimeTest {

    @Test
    public final void testTime() throws IOException, InterruptedException, ExecutionException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = "";
        server.createContext(context,new TemporaryHandler(server, HttpExchange::close, 1000));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        Thread.sleep(2000);

        final String url = "http://localhost:" + port + context;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        Assert.assertTrue("Server did not remove temporary context", server.getContexts().isEmpty());

        server.stop();
    }

}
