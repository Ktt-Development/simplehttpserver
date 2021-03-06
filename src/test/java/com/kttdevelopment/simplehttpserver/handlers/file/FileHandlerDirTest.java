package com.kttdevelopment.simplehttpserver.handlers.file;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public final class FileHandlerDirTest {

    @TempDir
    public final File dir = new File(UUID.randomUUID().toString());

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public final void addAndRemoveTest() throws IOException, InterruptedException{
        final int port = 8080;
        final SimpleHttpServer server   = SimpleHttpServer.create(port);
        final FileHandler handler       = new FileHandler();
        final String contextNoName      = "alt";
        final String contextWName       = "altn";
        final String dirNewName         = "dirName";

        final String fileName = "file";
        final String testContent = String.valueOf(System.currentTimeMillis());

        final File subdir   = new File(dir, UUID.randomUUID().toString());
        Assertions.assertTrue(subdir.exists() || subdir.mkdirs());

        final File file = new File(dir, fileName);
        Files.write(file.toPath(), testContent.getBytes());
        final File walk = new File(subdir, fileName);
        Files.write(walk.toPath(), testContent.getBytes());

        final String context = "";
        final String empty = handler.toString();

        handler.addDirectory(dir); // test file & directory read
        handler.addDirectory(contextNoName, dir);
        handler.addDirectory(dir, dirNewName);
        handler.addDirectory(contextWName, dir, dirNewName);

        server.createContext(context, handler);
        server.start();

        final String[] validPathsToTest = { // valid reads
            dir.getName()       + '/' + "file",
            contextNoName       + '/' + dir.getName()   + '/' + fileName,
            dirNewName          + '/' + fileName,
            contextWName        + '/' + dirNewName      + '/' + fileName
        };

        for(final String path : validPathsToTest){
            final String url = "http://localhost:" + port + '/' + path;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final String response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .get();

                Assertions.assertNotNull(response, "Client did not find data for " + path);
                Assertions.assertEquals(testContent, response, "Client data did not match server data for " + path);
            }catch(final ExecutionException ignored){
                Assertions.fail("Client did not find data for " + path);
            }
        }

        final String[] invalidPathsToTest = {
            dir.getName() + '/' + subdir.getName(),
            dir.getName() + '/' + subdir.getName() + '/' + fileName
        };

        for(final String path : invalidPathsToTest){
            final String url = "http://localhost:" + port + '/' +  path;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            Exception exception = null;
            try{
                final String response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .get();

                Assertions.assertNull(response, "Client found data for blocked path " + path);
            }catch(final ExecutionException e){
                exception = e;
            }
            Assertions.assertNotNull(exception, "Client found data for blocked path");
        }

        server.stop();

        handler.removeDirectory(dir); // test file & directory read
        handler.removeDirectory(contextNoName, dir);
        handler.removeDirectory(dirNewName);
        handler.removeDirectory(contextWName + '/' + dirNewName);
        Assertions.assertEquals(empty, handler.toString());
    }

}
