package simplehttpexchange;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import com.sun.net.httpserver.HttpContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleHttpExchangeReadTests {

    @Test
    public void read() throws IOException, InterruptedException, ExecutionException{
        final int port = 20001;

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

        String url = "http://localhost:" + port + context;

        final String headerKey = "header_key", headerValue = "header_value";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header(headerKey,headerValue)
            .build();

        String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenApply(HttpResponse::body).get();

        Assert.assertEquals("Response body did not match response sent",exchangeResponse.get(),response);

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Server from exchange was not equal to server issuing request",server.getHttpServer(),exchange.getHttpServer());
        Assert.assertNotNull("Native http exchange should not be null",exchange.getHttpExchange());

        Assert.assertEquals("Request context was not equal to handler context",context,exchange.getURI().getPath());

        Assert.assertNotNull("Client issuing request should not be null",exchange.getPublicAddress());
        Assert.assertNotNull("Local address should be local address",exchange.getLocalAddress());

        Assert.assertEquals("HttpContext from handler should be same as request", contextRef.get(),exchange.getHttpContext());

        Assert.assertEquals("HttpProtocol should've been HTTP/1.1 for this request","HTTP/1.1",exchange.getProtocol());

        Assert.assertEquals("Client header did not match exchange header",headerValue,exchange.getRequestHeaders().getFirst(headerKey));

        Assert.assertFalse("Client did not send a GET request",exchange.hasGet());
        Assert.assertFalse("Client did not send a POST request",exchange.hasPost());

        Assert.assertEquals("Successful exchange should've sent 200 HTTP OK", HttpCode.HTTP_OK,exchange.getResponseCode());

        Assert.assertTrue("Client did not send any cookies",exchange.getCookies().isEmpty());

        server.stop();

    }

}
