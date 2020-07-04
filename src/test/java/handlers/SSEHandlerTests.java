package handlers;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.SSEHandler;
import org.junit.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.*;

public class SSEHandlerTests {

    @SuppressWarnings({"unchecked", "Convert2Lambda", "rawtypes"})
    @Test
    public void test() throws IOException, ExecutionException, InterruptedException{
        final int port = 30006;

        final SimpleHttpServer server = SimpleHttpServer.create(port);

        final SSEHandler handler = new SSEHandler();
        server.createContext("",handler);
        server.start();

        // todo: connect to sse

        HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create("http://localhost:" + port))
             .timeout(Duration.ofSeconds(2))
             .build();

        final HttpClient client = HttpClient.newHttpClient();
        HttpResponse<InputStream> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()).get();

        final String result = "result";
        handler.push(result);

        BufferedReader IN = new BufferedReader(new InputStreamReader(response.body()));
        StringBuilder OUT = new StringBuilder();

        final Duration dur = Duration.ofSeconds(2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<String> future = executor.submit(new Callable() {
            @Override
            public String call(){
                String ln;
                try{
                    while((ln = IN.readLine()) != null)
                            OUT.append(ln).append('\n');
                }catch(IOException e){
                    Assert.fail("Unable to read input stream from client");
                }
                return OUT.toString();
            }
        });

        try {
            future.get(dur.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        }

        Assert.assertTrue("Client event stream did not match server event",OUT.toString().contains("id: 0\ndata: " + result));

        server.stop();
    }

}
