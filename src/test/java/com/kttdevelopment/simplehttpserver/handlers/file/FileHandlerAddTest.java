package com.kttdevelopment.simplehttpserver.handlers.file;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.ByteLoadingOption;
import com.kttdevelopment.simplehttpserver.handler.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;

public final class FileHandlerAddTest {

    @TempDir
    public final File dir = new File(UUID.randomUUID().toString());

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public final void addFileTests() throws IOException{
        final int port = 8080;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final FileHandler handler     = new FileHandler();
        final String context          = "";

        final Map<File, ByteLoadingOption> files = new HashMap<>();
        for(final ByteLoadingOption blop : ByteLoadingOption.values())
            files.put(new File(dir, blop.name()), blop);

        // initial write
        final String testContent = String.valueOf(System.currentTimeMillis());
        files.forEach((file, loadingOption) -> {
            try{
                Files.write(file.toPath(), testContent.getBytes());
                handler.addFile(file, loadingOption);
            }catch(final IOException e){
                e.printStackTrace();
            }
        });

        server.createContext(context, handler);
        server.start();

        files.forEach((file, loadingOption) -> {
            final String url = "http://localhost:" + port + context + '/' + file.getName();
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
                Assertions.assertEquals(testContent, response, "Client data did not match server data for " + file.getName());
            }catch(final InterruptedException | ExecutionException ignored){
                Assertions.fail("Failed to read context of " + file.getName());
            }

            // second write

            final String after = String.valueOf(System.currentTimeMillis());
            Assertions.assertDoesNotThrow(() -> Files.write(file.toPath(), after.getBytes()), "Failed to second write file " + file.getPath());

            try{
                final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assertions.assertEquals( loadingOption == ByteLoadingOption.PRELOAD ? testContent : after, response, "Client data did not match server data for " + file.getName());
            }catch(final InterruptedException | ExecutionException ignored){
                Assertions.fail("Failed to read context " + file.getName());
            }
        });

        server.stop();
    }

}
