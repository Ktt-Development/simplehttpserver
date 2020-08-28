package handlers.file;

import com.kttdevelopment.core.tests.TestUtil;
import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.FileHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("SpellCheckingInspection")
public final class FileHandlerWalkTest {

    @Test
    public final void addDirectoryTestsWalk() throws IOException, InterruptedException{
        final int port = 8080;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final FileHandler handler     = new FileHandler();

        final String testRealFile   = "test.txt", testFileContent = "readme";
        final String readDir        = "dirnoread";
        final String innerFile      = "innerNoRead.txt";

        final File noWalk = new File("src/test/resources/directory/nowalk");

        TestUtil.createTestFile(new File(noWalk, testRealFile), testFileContent);
        TestUtil.createTestFile(new File(new File(noWalk,readDir),innerFile),testFileContent);

        final String context = "";

        handler.addDirectory(noWalk,true);

        server.createContext(context,handler);
        server.start();

        final String[] validPathsToTest = { // valid reads
            noWalk.getName() + '/' + testRealFile,
            noWalk.getName() + '/' + readDir + '/' + innerFile
        };

        for(final String path : validPathsToTest){
            final String url = "http://localhost:" + port + '/' + path;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNotNull("Client did not find data for " + path,response);
                Assert.assertEquals("Client data did not match server data for " + path,testFileContent,response);
            }catch(final ExecutionException ignored){
                Assert.fail("Client did not find data for " + path);
            }
        }

        final String[] invalidPathsToTest = {
            noWalk.getName() + '/' + readDir
        };

        for(final String path : invalidPathsToTest){
            final String url = "http://localhost:" + port + '/' +  path;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            Exception exception = null;
            try{
                String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNull("Client found data for blocked path " + path,response);
            }catch(final ExecutionException e){
                exception = e;
            }
            Assert.assertNotNull("Client found data for blocked path",exception);
        }

        server.stop();
    }

}
