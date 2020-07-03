package simplehttpexchange;

import com.kttdevelopment.simplehttpserver.*;
import com.sun.net.httpserver.HttpContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleHttpExchangeCookieSessionTests {

    @Test
    public void testSession() throws IOException, ExecutionException, InterruptedException{
        final int port = 20004;

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
        final String cookie = "__session-id";
        server.setHttpSessionHandler(new HttpSessionHandler(cookie));
        server.start();

        String url = "http://localhost:" + port + context ;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        final CookieManager cookies = new CookieManager();
        HttpClient client = HttpClient.newBuilder()
            .cookieHandler(cookies)
            .build();

        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();
        Assert.assertEquals("Response body did not match response sent", exchangeResponse.get(), response);


        // exchange

        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertNotNull("Client was not assigned a http session",server.getHttpSession(exchange));
        final String sessionId = server.getHttpSession(exchange).getSessionID();
        Assert.assertEquals("Http Session ID did not match cookie session id",sessionId,cookies.getCookieStore().get(URI.create(url)).get(0).getValue());

        final HttpSession session = server.getHttpSession(exchange);
        Assert.assertTrue("Client session creation time was in the future",session.getCreationTime() < System.currentTimeMillis());
        Assert.assertTrue("Client session last access time was in the future",session.getLastAccessTime() < System.currentTimeMillis());

        server.stop();
    }

}
