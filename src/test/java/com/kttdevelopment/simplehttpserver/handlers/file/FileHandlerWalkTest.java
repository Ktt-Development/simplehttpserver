package com.kttdevelopment.simplehttpserver.handlers.file;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.FileHandler;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("SpellCheckingInspection")
public final class FileHandlerWalkTest {

    @Rule
    public final TemporaryFolder directory = new TemporaryFolder(new File("src/test/resources"));

    @Test
    public final void addDirectoryTestsWalk() throws IOException, InterruptedException{
        final int port = 8080;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final FileHandler handler     = new FileHandler();

        final String fileName       = "file";
        final String testContent    = String.valueOf(System.currentTimeMillis());

        final File dir      = directory.getRoot();
        final File subdir   = directory.newFolder();

        final File file = new File(directory.getRoot(),fileName);
        Files.write(file.toPath(), testContent.getBytes());
        final File walk = new File(subdir,fileName);
        Files.write(walk.toPath(),testContent.getBytes());

        final String context = "";

        handler.addDirectory(dir,true);

        server.createContext(context,handler);
        server.start();

        final String[] validPathsToTest = { // valid reads
            dir.getName() + '/' + file.getName(),
            dir.getName() + '/' + subdir.getName() + '/' + walk.getName()
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

                Assert.assertNotNull("Client did not find data for " + path,response);
                Assert.assertEquals("Client data did not match server data for " + path,testContent,response);
            }catch(final ExecutionException ignored){
                Assert.fail("Client did not find data for " + path);
            }
        }

        final String[] invalidPathsToTest = {
            dir.getName() + '/' + subdir.getName()
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

                Assert.assertNull("Client found data for blocked path " + path,response);
            }catch(final ExecutionException e){
                exception = e;
            }
            Assert.assertNotNull("Client found data for blocked path",exception);
        }

        server.stop();
    }

}
