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

public final class SimpleHttpExchangePostTest {

    @Test
    public final void postSimple() throws IOException, ExecutionException, InterruptedException{
        final int port = 20003;

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
        final String url = "http://localhost:" + port + context ;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(queryKey + '=' + queryValue))
            .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Client request method did not match exchange request method (POST)", RequestMethod.POST, exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client POST map", exchange.hasPost());
        Assert.assertEquals("Exchange POST did not match client POST", queryValue, exchange.getPostMap().get(queryKey));

        server.stop();
    }

}
