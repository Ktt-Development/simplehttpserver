package simplehttpexchange;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.RequestMethod;
import com.sun.net.httpserver.HttpContext;
import org.junit.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleHttpExchangeGetPostTests {

    @Test
    public void get() throws IOException, ExecutionException, InterruptedException{
        final int port = 20002;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final AtomicReference<SimpleHttpExchange> exchangeRef = new AtomicReference<>();
        final AtomicReference<String> exchangeResponse = new AtomicReference<>();
        final SimpleHttpHandler handler = exchange -> {
            exchangeRef.set(exchange);
            exchangeResponse.set(exchange.toString());
            exchange.send(exchange.toString());
        };

        final String context = server.getRandomContext();
        final AtomicReference<HttpContext> contextRef = new AtomicReference<>();
        contextRef.set(server.createContext(context,handler));
        server.start();

        String queryKey = "test", queryValue = "value";
        String url = "http://localhost:" + port + context + '?' + queryKey + '=' + queryValue;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .GET()
             .build();

        String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Response body did not match response sent", exchangeResponse.get(), response);

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Client request method did not match exchange request method (GET)", RequestMethod.GET,exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client GET map", exchange.hasGet());
        Assert.assertEquals("Exchange GET did not match client GET", queryValue, exchange.getGetMap().get(queryKey));
    }

    @Test
    public void postSimple() throws IOException, ExecutionException, InterruptedException{
        final int port = 20003;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final AtomicReference<SimpleHttpExchange> exchangeRef = new AtomicReference<>();
        final AtomicReference<String> exchangeResponse = new AtomicReference<>();
        final SimpleHttpHandler handler = exchange -> {
            exchangeRef.set(exchange);
            exchangeResponse.set(exchange.toString());
            exchange.send(exchange.toString());
        };

        final String context = server.getRandomContext();
        final AtomicReference<HttpContext> contextRef = new AtomicReference<>();
        contextRef.set(server.createContext(context,handler));
        server.start();

        String queryKey = "test", queryValue = "value";
        String url = "http://localhost:" + port + context ;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .POST(HttpRequest.BodyPublishers.ofString(queryKey + '=' + queryValue))
             .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Client request method did not match exchange request method (POST)", RequestMethod.POST,exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client POST map", exchange.hasPost());
        Assert.assertEquals("Exchange POST did not match client POST", queryValue, exchange.getPostMap().get(queryKey));

        server.stop();
    }

    @Test @Ignore
    public void postMultipartFormData() throws IOException, ExecutionException, InterruptedException{
        final int port = 20005;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final AtomicReference<SimpleHttpExchange> exchangeRef = new AtomicReference<>();
        final AtomicReference<String> exchangeResponse = new AtomicReference<>();
        final SimpleHttpHandler handler = exchange -> {
            exchangeRef.set(exchange);
            exchangeResponse.set(exchange.toString());
            exchange.send(exchange.toString());
        };

        final String context = server.getRandomContext();
        final AtomicReference<HttpContext> contextRef = new AtomicReference<>();
        contextRef.set(server.createContext(context,handler));
        server.start();


        String url = "http://localhost:" + port + context ;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             // .POST(HttpRequest.BodyPublishers.ofString(queryKey + '=' + queryValue)) // todo: load file
             .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Client request method did not match exchange request method (POST)", RequestMethod.POST,exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client POST map", exchange.hasPost());
        // todo: check if value matches load file
        // Assert.assertEquals("Exchange POST did not match client POST", queryValue, exchange.getPostMap().get(queryKey));

        server.stop();
    }

}
