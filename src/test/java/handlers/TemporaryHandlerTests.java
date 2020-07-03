package handlers;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.TemporaryHandler;
import com.sun.net.httpserver.HttpContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class TemporaryHandlerTests {

    @Test
    public void testTime() throws IOException, InterruptedException, ExecutionException{
        final int port = 30001;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = server.getRandomContext();
        server.createContext(context,new TemporaryHandler((SimpleHttpHandler) SimpleHttpExchange::close, 1000));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        Thread.sleep(2000);

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        int response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        // BUG!!!!
        // Assert.assertTrue("Server did not remove temporary context", server.getContexts().isEmpty());
        Exception exception = null;
        try{ server.getHttpServer().removeContext(context);
        }catch(IllegalArgumentException e){ exception = e; }
        Assert.assertNotNull("Server did not remove temporary context",exception);
        // an exception should exist if removing already removed
    }

    @Test
    public void testFirstConn() throws ExecutionException, InterruptedException, IOException{
        final int port = 30002;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = server.getRandomContext();
        server.createContext(context,new TemporaryHandler((SimpleHttpHandler) SimpleHttpExchange::close));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        int response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        // BUG!!!!
        // Assert.assertTrue("Server did not remove temporary context", server.getContexts().isEmpty());
        Exception exception = null;
        try{ server.getHttpServer().removeContext(context);
        }catch(IllegalArgumentException e){ exception = e; }
        Assert.assertNotNull("Server did not remove temporary context",exception);
        // an exception should exist if removing already removed

        server.stop();
    }

}
