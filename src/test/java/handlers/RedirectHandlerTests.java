package handlers;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.RedirectHandler;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public class RedirectHandlerTests {

    @Test
    public void redirectToGoogle() throws IOException, ExecutionException, InterruptedException{
        final int port = 30003;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = server.getRandomContext();
        server.createContext(context,new RedirectHandler("https://www.google.com/"));
        server.start();

        Assert.assertFalse("Server did not contain a temporary context", server.getContexts().isEmpty());

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

        final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

        int response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
             .thenApply(HttpResponse::statusCode).get();

        Assert.assertEquals("Client did not respond with redirect code (302 HTTP FOUND)",HttpCode.HTTP_OK, response);

        server.stop();
    }

}
