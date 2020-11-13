package com.kttdevelopment.simplehttpserver.handlers.throttler;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.*;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.*;

public final class ServerExchangeThrottlerTest {

    @Test
    public final void serverExchangeThrottler() throws IOException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        server.setExecutor(Executors.newCachedThreadPool());

        final String context = "";
        server.createContext(
            context,
            new ThrottledHandler(
                (SimpleHttpHandler) exchange -> {
                    try{ Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                    }catch(final InterruptedException ignored){ }
                    exchange.send(exchange.toString());
                },
                new ServerExchangeThrottler(){
                    @Override
                    public final int getMaxConnections(final HttpExchange exchange){
                        return 1;
                    }
                }
            )
        );
        server.start();

        final String url = "http://localhost:" + port + context;

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(1))
            .build();

        new Thread(() -> {
            try{
                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode).get();
            }catch(final InterruptedException | ExecutionException ignored){ }
        }).start();

        Assertions.assertThrows(ExecutionException.class, () -> HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode).get(), "Second request returned a result for a throttled thread (connection not allowed)");
        server.stop();
    }

}
