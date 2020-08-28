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

public final class FileHandlerNoWalkTest {

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public final void addDirectoryTestsNoWalkAdapter() throws IOException, InterruptedException{
        final int port = 8080;
        final SimpleHttpServer server       = SimpleHttpServer.create(port);
        final FileHandlerAdapter adapter    = new FileHandlerAdapter() {
            @Override
            public final byte[] getBytes(final File file, final byte[] bytes){
                return bytes;
            }

            @Override
            public final String getName(final File file){
                return file.getName().substring(0,file.getName().lastIndexOf('.'));
            }
        };
        final FileHandler handler           = new FileHandler(adapter);
        final String contextNoName          = "alt";
        final String contextWName           = "altn";
        final String dirNewName             = "dirName";

        final String testRealFile   = "test", testFileContent = "readme";
        final String noReadDir      = "dirnoread";
        final String noReadFile     = "innerNoRead";

        final File noWalk = new File("src/test/resources/directory/nowalk");

        TestUtil.createTestFile(new File(noWalk, testRealFile + ".txt"), testFileContent);
        TestUtil.createTestFile(new File(new File(noWalk,noReadDir),noReadFile + ".txt"),testFileContent);

        final String context = "";

        handler.addDirectory(noWalk); // test file & directory read
        handler.addDirectory(contextNoName,noWalk);
        handler.addDirectory(noWalk,dirNewName);
        handler.addDirectory(contextWName,noWalk,dirNewName);

        server.createContext(context,handler);
        server.start();

        final String[] validPathsToTest = { // valid reads
            noWalk.getName()    + '/' + testRealFile,
            contextNoName       + '/' + noWalk.getName()  + '/' + testRealFile,
            dirNewName          + '/' + testRealFile,
            contextWName        + '/' + dirNewName + '/' + testRealFile
        };

        for(final String path : validPathsToTest){
            final String url = "http://localhost:" + port + '/' + path;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNotNull("Client did not find data for " + path, response);
                Assert.assertEquals("Client data did not match server data for " + path,testFileContent,response);
            }catch(final ExecutionException ignored){
                Assert.fail("Client did not find data for " + path);
            }
        }

        final String[] invalidPathsToTest = {
            noWalk.getName() + '/' + noReadDir,
            noWalk.getName() + '/' + noReadDir + '/' + noReadFile
        };

        for(final String path : invalidPathsToTest){
            final String url = "http://localhost:" + port + '/' +  path;
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            Exception exception = null;
            try{
                final String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
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
