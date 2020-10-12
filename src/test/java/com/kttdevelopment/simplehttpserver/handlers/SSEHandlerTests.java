package com.kttdevelopment.simplehttpserver.handlers;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.SSEHandler;
import org.junit.*;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.*;

public final class SSEHandlerTests {

    @SuppressWarnings({"unchecked", "Convert2Lambda", "rawtypes"})
    @Test
    public final void test() throws IOException, ExecutionException, InterruptedException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        String context = "";

        final SSEHandler handler = new SSEHandler();
        server.createContext(context,handler);
        server.start();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port))
            .timeout(Duration.ofSeconds(2))
            .build();

        final HttpClient client = HttpClient.newHttpClient();
        HttpResponse<InputStream> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()).get();

        final String result = "result";
        handler.push(result);

        final BufferedReader IN = new BufferedReader(new InputStreamReader(response.body()));
        final StringBuilder OUT = new StringBuilder();

        final Duration dur = Duration.ofSeconds(2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<String> future = executor.submit(new Callable() {
            @Override
            public final String call(){
                String ln;
                try{
                    while((ln = IN.readLine()) != null)
                        OUT.append(ln).append('\n');
                }catch(final IOException ignored){
                    Assert.fail("Unable to read input stream from client");
                }
                return OUT.toString();
            }
        });

        try{
            future.get(dur.toMillis(), TimeUnit.MILLISECONDS);
        }catch(final TimeoutException ignored){
        }finally{
            future.cancel(true);
        }

        Assert.assertTrue("Client event stream did not match server event",OUT.toString().contains("id: 0\ndata: " + result));

        server.stop();
    }

}
