package simplehttpexchange;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.var.RequestMethod;
import com.sun.net.httpserver.HttpContext;
import org.junit.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;
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

        final String context = "";
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

        final String context = "";
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

    @SuppressWarnings({"rawtypes", "StringBufferReplaceableByString"})
    @Test
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

        final String context = "";
        server.createContext(context,handler);
        server.start();

        String boundary = "d74496d66958873e";

        String url = "http://localhost:" + port + context ;

        String key = "key", value = "value";
        String fkey = "fileKey", filename = "fileName.txt", fvalue = "fileValue";

        StringBuilder OUT = new StringBuilder();
        OUT.append("--------------------------").append(boundary).append("\r\n");
        OUT.append("Content-Disposition: ").append("form-data; ").append("name=\"").append(key).append('\"').append("\r\n\r\n");
        OUT.append(value).append("\r\n");
        OUT.append("--------------------------").append(boundary).append("\r\n");
        OUT.append("Content-Disposition: ").append("form-data; ").append("name=\"").append(fkey).append("\"; ");
        OUT.append("filename=\"").append(filename).append('\"').append('\n');
        OUT.append("Content-Type: ").append("text/plain").append("\r\n\r\n");
        OUT.append(fvalue).append("\r\n");
        OUT.append("--------------------------").append(boundary).append("--");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-type","multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofString(OUT.toString()))
            .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body).get();

        // exchange
        final SimpleHttpExchange exchange = exchangeRef.get();

        Assert.assertEquals("Client request method did not match exchange request method (POST)", RequestMethod.POST,exchange.getRequestMethod());
        Assert.assertTrue("Exchange was missing client POST map", exchange.hasPost());

        Assert.assertEquals("Client form value did not match server value",value, ((Map) exchange.getPostMap().get(key)).get("value"));
        Assert.assertEquals("Client file name did not match server value",filename, ((Map) ((Map) ((Map) ((Map) exchange.getPostMap().get(fkey)).get("headers")).get("Content-Disposition")).get("parameters")).get("filename"));
        Assert.assertEquals("Client file value did not match server value",fvalue, ((Map) exchange.getPostMap().get(fkey)).get("value"));

        server.stop();
    }

}
