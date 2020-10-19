package com.kttdevelopment.simplehttpserver.handlers.file;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class FileHandlerCacheTest {

    @Test
    public final void testIllegalArg(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileHandler(new FileHandlerAdapter() {}).addFile(new File("null"), ByteLoadingOption.CACHELOAD));
        Assertions.assertDoesNotThrow(() -> new FileHandler(new CacheFileAdapter(0)).addFile(new File("null")));
        Assertions.assertDoesNotThrow(() -> new FileHandler(new CacheFileAdapter(0)).addFile(new File("null"), ByteLoadingOption.CACHELOAD));
    }

    @TempDir
    public final File dir = new File(UUID.randomUUID().toString());

    @SuppressWarnings({"DuplicateExpressions", "RedundantSuppression"})
    @Test
    public final void testCacheLoad() throws IOException, InterruptedException, ExecutionException{
        final int port = 8080;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final long cacheTime = 5 * 1000;
        final String content = UUID.randomUUID().toString();

        final FileHandler handler = new FileHandler(new CacheFileAdapter(cacheTime));
        final File file = new File(dir, UUID.randomUUID().toString());
        final String filename = file.getName();
        Files.write(file.toPath(), content.getBytes());

        final File file2 = new File(dir, UUID.randomUUID().toString());
        final String content2 = UUID.randomUUID().toString();
        Files.write(file2.toPath(), content2.getBytes());

        final File file3 = new File(dir, UUID.randomUUID().toString());
        Assertions.assertTrue(file3.createNewFile());
        handler.addFile(file, ByteLoadingOption.CACHELOAD);
        handler.addFile(file3);

        server.createContext("", handler);
        server.start();

        final String check = "bytes=" + Arrays.toString(content.getBytes());
        final String check2 = "bytes=" + Arrays.toString(content2.getBytes());
        // access new file
        Assertions.assertFalse(handler.toString().contains(check));
        Assertions.assertFalse(handler.toString().contains(check2));
        try{
            final String url = "http://localhost:" + port + '/' + filename;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            final String response = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get();

            Assertions.assertNotNull(response, "Client did not find data for " + filename);
            Assertions.assertEquals(content, response, "Client data did not match server data for " + filename);
        }catch(final ExecutionException ignored){
            Assertions.fail("Client did not find data for " + filename);
        }
        Assertions.assertTrue(handler.toString().contains(check));
        Assertions.assertFalse(handler.toString().contains(check2));

        // check if file is cleared after expiry
        Thread.sleep(cacheTime + 1000);
        {
            final String url = "http://localhost:" + port + '/' + file3.getName();
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get();

            Assertions.assertFalse(handler.toString().contains(check));
            Assertions.assertFalse(handler.toString().contains(check2));
        }
        // access file again
        try{
            final String url = "http://localhost:" + port + '/' + filename;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            final String response = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get();

            Assertions.assertNotNull(response, "Client did not find data for " + filename);
            Assertions.assertEquals(content, response, "Client data did not match server data for " + filename);
        }catch(final ExecutionException ignored){
            Assertions.fail("Client did not find data for " + filename);
        }
        Assertions.assertTrue(handler.toString().contains(check));
        Assertions.assertFalse(handler.toString().contains(check2));
    }

}
