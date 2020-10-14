package com.kttdevelopment.simplehttpserver.simplehttpexchange.io;

import com.kttdevelopment.simplehttpserver.SimpleHttpHandler;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.var.HttpCode;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class SimpleHttpExchangeSendTest {

    @Rule
    public final TemporaryFolder directory = new TemporaryFolder(new File("."));

    @Test
    public final void sendTest() throws IOException{
        final int port = 8080;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final String context          = "";

        final int testCode = HttpCode.HTTP_Accepted;
        final String testContent = String.valueOf(System.currentTimeMillis());

        server.createContext("code", (SimpleHttpHandler) exchange -> {
            exchange.send(testCode);
            exchange.close();
        });
        server.createContext("bytes", (SimpleHttpHandler) exchange -> exchange.send(testContent.getBytes()));
        server.createContext("bytes/gzip", (SimpleHttpHandler) exchange -> exchange.send(testContent.getBytes(),true));
        server.createContext("string", (SimpleHttpHandler) exchange -> exchange.send(testContent));
        server.createContext("string/gzip", (SimpleHttpHandler) exchange -> exchange.send(testContent,true));

        final File testFile = directory.newFile();
        Files.write(testFile.toPath(),testContent.getBytes());
        server.createContext("file", (SimpleHttpHandler) exchange -> exchange.send(testFile));
        server.createContext("file/gzip", (SimpleHttpHandler) exchange -> exchange.send(testFile,true));

        server.start();

        // response code
        {
            final String url = "http://localhost:" + port + context + '/' + "code";
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final int response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode).get();
                Assert.assertEquals("Client data did not match server data for " + url, testCode, response);
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        // bytes
        {
            final String url = "http://localhost:" + port + context + '/' + "bytes";
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final String response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
                Assert.assertEquals("Client data did not match server data for " + url, testContent, response);
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        // bytes gzip
        {
            final String url = "http://localhost:" + port + context + '/' + "bytes" + '/' + "gzip";
            final HttpRequest request = HttpRequest.newBuilder()
                .header("Accept-Encoding","deflate, gzip")
                .uri(URI.create(url))
                .build();

            try{
                final HttpHeaders response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::headers).get();
                Assert.assertEquals("Client data did not match server data for " + url, "gzip", response.firstValue("Content-Encoding").get());
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        // string
        {
            final String url = "http://localhost:" + port + context + '/' + "string";
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final String response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
                Assert.assertEquals("Client data did not match server data for " + url, testContent, response);
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        // string gzip
        {
            final String url = "http://localhost:" + port + context + '/' + "string" + '/' + "gzip";
            final HttpRequest request = HttpRequest.newBuilder()
                .header("Accept-Encoding","deflate, gzip")
                .uri(URI.create(url))
                .build();

            try{
                final HttpHeaders response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::headers).get();
                Assert.assertEquals("Client data did not match server data for " + url, "gzip", response.firstValue("Content-Encoding").get());
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        // file
        {
            final String url = "http://localhost:" + port + context + '/' + "file";
            final HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(url))
               .build();

            try{
                final String response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
                Assert.assertEquals("Client data did not match server data for " + url, testContent, response);
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        // file gzip
        {
            final String url = "http://localhost:" + port + context + '/' + "file" + '/' + "gzip";
            final HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(url))
               .build();

            try{
                final HttpHeaders response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::headers).get();
                Assert.assertEquals("Client data did not match server data for " + url, "gzip", response.firstValue("Content-Encoding").get());
            }catch(final InterruptedException | ExecutionException ignored){
                Assert.fail("Failed to read context for " + url);
            }
        }

        server.stop();
    }

}
