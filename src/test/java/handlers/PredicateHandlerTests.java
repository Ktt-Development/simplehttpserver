package handlers;

import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.PredicateHandler;
import com.kttdevelopment.simplehttpserver.handler.RootHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public class PredicateHandlerTests {

    @Test
    public void predicate() throws IOException, ExecutionException, InterruptedException{
        final int port = 30004;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final boolean condition = System.currentTimeMillis() % 2 == 1;

        final String context = "";
        server.createContext(context,new PredicateHandler((SimpleHttpHandler) exchange -> exchange.send("A"), (SimpleHttpHandler) exchange -> exchange.send("B"), exchange -> condition));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
             .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Server response did not match client response", condition ? "A" : "B", response);

        server.stop();
    }

    @Test
    public void root() throws IOException, ExecutionException, InterruptedException{
        final int port = 30005;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        String context = "";
        server.createContext(context,new RootHandler((SimpleHttpHandler) exchange -> exchange.send("A"), (SimpleHttpHandler) exchange -> exchange.send("B")));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        String url = "http://localhost:" + port;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
             .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Server root response did not match client root response","A",response);

        request = HttpRequest.newBuilder()
             .uri(URI.create(url + server.getRandomContext()))
             .build();

        response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
             .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Server else response did not match client else response","B",response);

        server.stop();
    }

}
