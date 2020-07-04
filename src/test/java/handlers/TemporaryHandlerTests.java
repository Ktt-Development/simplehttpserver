package handlers;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.TemporaryHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public class TemporaryHandlerTests {

    @Test
    public void testTime() throws IOException, InterruptedException, ExecutionException{
        final int port = 30001;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = server.getRandomContext();
        server.createContext(context,new TemporaryHandler(server,(SimpleHttpHandler) SimpleHttpExchange::close, 1000));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        Thread.sleep(2000);

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        Assert.assertTrue("Server did not remove temporary context", server.getContexts().isEmpty());

        server.stop();
    }

    @Test
    public void testFirstConn() throws ExecutionException, InterruptedException, IOException{
        final int port = 30002;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = server.getRandomContext();
        server.createContext(context,new TemporaryHandler(server,(SimpleHttpHandler) SimpleHttpExchange::close));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::statusCode).get();

        Assert.assertTrue("Server did not remove temporary context", server.getContexts().isEmpty());

        server.stop();
    }

}
