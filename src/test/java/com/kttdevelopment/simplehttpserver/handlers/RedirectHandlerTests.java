package com.kttdevelopment.simplehttpserver.handlers;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.RedirectHandler;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public final class RedirectHandlerTests {

    @Test
    public final void redirectToGoogle() throws IOException, ExecutionException, InterruptedException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = "";
        server.createContext(context,new RedirectHandler("https://www.google.com/"));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        final String url = "http://localhost:" + port + context;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

        final int response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        Assert.assertEquals("Client responded with redirect code (302 HTTP FOUND) not 200 HTTP OK",HttpCode.HTTP_OK, response);

        server.stop();
    }

}
