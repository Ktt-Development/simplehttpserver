package handlers;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.*;
import com.sun.net.httpserver.HttpExchange;
import org.junit.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.*;

public class ThrottledHandlerTests {

    @Test
    public void sessionThrottler() throws IOException{
        final int port = 30010;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final HttpSessionHandler sessionHandler = new HttpSessionHandler();
        server.setHttpSessionHandler(sessionHandler);

        final String context = "";
        server.createContext(
            context,
            new ThrottledHandler(
                (SimpleHttpHandler) exchange -> {
                    try{ Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                    }catch(InterruptedException ignored){ }
                    exchange.send(exchange.toString());
                },
                new SessionThrottler(sessionHandler){
                    @Override
                    public int getMaxConnections(final HttpSession session, final HttpExchange exchange){
                        return 1;
                    }
                }
            )
        );
        server.start();

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .timeout(Duration.ofSeconds(1))
             .build();

        new Thread(() -> {
            try{
                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode).get();
            }catch(InterruptedException | ExecutionException ignored){ }
        }).start();

        Exception exception = null;
        try{
            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                      .thenApply(HttpResponse::statusCode).get();
        }catch(InterruptedException | ExecutionException e){ // JDK failure to recognize HttpTimeoutException as valid catch
            exception = e;
        }
        Assert.assertTrue("Second request returned a result for a throttled thread (connection not allowed)",exception instanceof ExecutionException);

        server.stop();
    }

    @Test
    public void serverSessionThrottler() throws IOException{
        final int port = 30015;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final HttpSessionHandler sessionHandler = new HttpSessionHandler();
        server.setHttpSessionHandler(sessionHandler);

        final String context = "";
        server.createContext(
            context,
            new ThrottledHandler(
                (SimpleHttpHandler) exchange -> {
                    try{ Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                    }catch(InterruptedException ignored){ }
                    exchange.send(exchange.toString());
                },
                new ServerSessionThrottler(sessionHandler){
                    @Override
                    public int getMaxConnections(final HttpSession session, final HttpExchange exchange){
                        return 1;
                    }
                }
            )
        );
        server.start();

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .timeout(Duration.ofSeconds(1))
             .build();

        new Thread(() -> {
            try{
                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode).get();
            }catch(InterruptedException | ExecutionException ignored){ }
        }).start();

        Exception exception = null;
        try{
            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode).get();
        }catch(InterruptedException | ExecutionException e){ // JDK failure to recognize HttpTimeoutException as valid catch
            exception = e;
        }
        Assert.assertTrue("Second request returned a result for a throttled thread (connection not allowed)",exception instanceof ExecutionException);

        server.stop();
    }

    @Test
    public void exchangeThrottler() throws IOException{
        final int port = 30012;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = "";
        server.createContext(
            context,
            new ThrottledHandler(
                (SimpleHttpHandler) exchange -> {
                    try{ Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                    }catch(InterruptedException ignored){ }
                    exchange.send(exchange.toString());
                },
                new ExchangeThrottler(){
                    @Override
                    public int getMaxConnections(final HttpExchange exchange){
                        return 1;
                    }
                }
            )
        );
        server.start();

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .timeout(Duration.ofSeconds(1))
             .build();

        new Thread(() -> {
            try{
                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode).get();
            }catch(InterruptedException | ExecutionException ignored){ }
        }).start();

        Exception exception = null;
        try{
            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode).get();
        }catch(InterruptedException | ExecutionException e){ // JDK failure to recognize HttpTimeoutException as valid catch
            exception = e;
        }
        Assert.assertTrue("Second request returned a result for a throttled thread (connection not allowed)",exception instanceof ExecutionException);

        server.stop();
    }

    @Test
    public void serverExchangeThrottler() throws IOException{
        final int port = 30013;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final String context = "";
        server.createContext(
            context,
            new ThrottledHandler(
                (SimpleHttpHandler) exchange -> {
                    try{ Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                    }catch(InterruptedException ignored){ }
                    exchange.send(exchange.toString());
                },
                new ServerExchangeThrottler(){
                    @Override
                    public int getMaxConnections(final HttpExchange exchange){
                        return 1;
                    }
                }
            )
        );
        server.start();

        String url = "http://localhost:" + port + context;

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .timeout(Duration.ofSeconds(1))
             .build();

        new Thread(() -> {
            try{
                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode).get();
            }catch(InterruptedException | ExecutionException ignored){ }
        }).start();

        Exception exception = null;
        try{
            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode).get();
        }catch(InterruptedException | ExecutionException e){ // JDK failure to recognize HttpTimeoutException as valid catch
            exception = e;
        }
        Assert.assertTrue("Second request returned a result for a throttled thread (connection not allowed)",exception instanceof ExecutionException);

        server.stop();
    }

}
