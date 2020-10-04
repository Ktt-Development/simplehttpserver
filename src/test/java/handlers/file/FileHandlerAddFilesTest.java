package handlers.file;

import com.kttdevelopment.core.tests.TestUtil;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.FileHandler;
import com.kttdevelopment.simplehttpserver.handler.FileHandlerAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public final class FileHandlerAddFilesTest {

    @Test
    public final void addFilesTests() throws IOException, ExecutionException, InterruptedException{
        final int port = 8080;
        final SimpleHttpServer server       = SimpleHttpServer.create(port);
        final String override               = "override";
        final String context                = "", altContext = "/alt";
        final FileHandlerAdapter adapter    = new FileHandlerAdapter() {
            @Override
            public final byte[] getBytes(final File file, final byte[] bytes){
                return override.getBytes();
            }

            @Override
            public final String getName(final File file){
                return file.getName().substring(0,file.getName().lastIndexOf('.'));
            }
        };
        final FileHandler handler = new FileHandler(adapter);

        final File[] files = new File[]{
            new File("src/test/resources/files/test.txt"),
            new File("src/test/resources/files/test2.txt")
        };

        for(final File file : files)
            TestUtil.createTestFile(file);

        handler.addFiles(files);
        handler.addFiles(altContext,files);

        server.createContext(context,handler);

        server.start();

        for(final File file : files){
            final String url = "http://localhost:" + port + context;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + file.getName().substring(0, file.getName().lastIndexOf('.'))))
                .build();

            String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).get();

            Assert.assertEquals("Adapter bytes did not match client received bytes", override, response);

            // alt

            final String altUrl = "http://localhost:" + port + altContext;
            request = HttpRequest.newBuilder()
                .uri(URI.create(altUrl + "/" + file.getName().substring(0,file.getName().lastIndexOf('.'))))
                .build();

            response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).get();

            Assert.assertEquals("Adapter alt context bytes did not match client received bytes",override,response);
        }

        server.stop();

    }

}