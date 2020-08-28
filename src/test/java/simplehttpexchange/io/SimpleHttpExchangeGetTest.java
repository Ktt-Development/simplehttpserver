package simplehttpexchange.io;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.RequestMethod;
import com.sun.net.httpserver.HttpContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public final class SimpleHttpExchangeGetTest {

    @Test
    public final void get() throws IOException, ExecutionException, InterruptedException{
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
        contextRef.set(server.createContext(context,handler));
        server.start();

        final String queryKey = "test", queryValue = "value";
        final String url = "http://localhost:" + port + context + '?' + queryKey + '=' + queryValue;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Response body did not match response sent", exchangeResponse.get(), response);

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Client request method did not match exchange request method (GET)", RequestMethod.GET, exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client GET map", exchange.hasGet());
        Assert.assertEquals("Exchange GET did not match client GET", queryValue, exchange.getGetMap().get(queryKey));
    }

}
