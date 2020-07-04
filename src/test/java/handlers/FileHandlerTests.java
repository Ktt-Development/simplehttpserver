package handlers;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.*;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutionException;

public class FileHandlerTests {

    @Test @Ignore
    public void addFileTests() throws IOException{ // test load types
        final int port = 25001;
        final SimpleHttpsServer server = SimpleHttpsServer.create(port);
        final FileHandler handler = new FileHandler();
    }

    @Test
    public void addFilesTests() throws IOException, ExecutionException, InterruptedException{ // run adapter tests here
        final int port = 25002;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final String override = "override";
        final String context = "", altContext = "/alt";
        final FileHandlerAdapter adapter = new FileHandlerAdapter() {
            @Override
            public byte[] getBytes(final File file, final byte[] bytes){
                return override.getBytes();
            }

            @Override
            public String getName(final File file){
                return file.getName().substring(0,file.getName().lastIndexOf('.'));
            }
        };
        final FileHandler handler = new FileHandler(adapter);

        final File[] files = new File[]{
            new File("src/test/resources/files/test.txt"),
            new File("src/test/resources/files/test2.txt")
        };

        handler.addFiles(files);
        handler.addFiles(altContext,files);

        server.createContext(context,handler);

        server.start();

        for(final File file : files){
            String url = "http://localhost:" + port + context;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + file.getName().substring(0,file.getName().lastIndexOf('.'))))
                .build();

            String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).get();

            Assert.assertEquals("Adapter bytes did not match client received bytes",override,response);

            // alt

            String altUrl = "http://localhost:" + port + altContext;
            request = HttpRequest.newBuilder()
                .uri(URI.create(altUrl + "/" + file.getName().substring(0,file.getName().lastIndexOf('.'))))
                .build();

            response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).get();

            Assert.assertEquals("Adapter alt context bytes did not match client received bytes",override,response);
        }

        server.stop();

    }

    @Test
    public void addDirectoryTestsNoWalk() throws IOException, InterruptedException{
        final int port = 25003;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final FileHandler handler = new FileHandler();
        final String contextNoName = "alt";
        final String contextWName = "altn";
        final String dirNewName = "dirName";

        final String testRealFile = "test.txt", testFileContent = "readme";
        final String noReadDir = "dirnoread";
        final String noReadFile = "innerNoRead.txt";

        final File noWalk = new File("src/test/resources/directory/nowalk");

        final String context = "";

        handler.addDirectory(noWalk); // test file & directory read
        handler.addDirectory(contextNoName,noWalk);
        handler.addDirectory(noWalk,dirNewName);
        handler.addDirectory(contextWName,noWalk,dirNewName);

        server.createContext(context,handler);
        server.start();

        final String[] validPathsToTest = { // valid reads
            noWalk.getName() + '/' + testRealFile,
            contextNoName + '/' + noWalk.getName()  + '/' + testRealFile,
            dirNewName + '/' + testRealFile,
            contextWName + '/' + dirNewName + '/' + testRealFile
        };

        for(final String path : validPathsToTest){
            String url = "http://localhost:" + port + '/' + path;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNotNull("Client did not find data for " + path,response);
                Assert.assertEquals("Client data did not match server data for " + path,testFileContent,response);
            }catch(ExecutionException e){
                Assert.fail("Client did not find data for " + path);
            }
        }

        final String[] invalidPathsToTest = {
            noWalk.getName() + '/' + noReadDir,
            noWalk.getName() + '/' + noReadDir + '/' + noReadFile
        };

        for(final String path : invalidPathsToTest){
            String url = "http://localhost:" + port + '/' +  path;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            Exception exception = null;
            try{
                String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNull("Client found data for blocked path " + path,response);
            }catch(ExecutionException e){
                exception = e;
            }
            Assert.assertNotNull("Client found data for blocked path",exception);
        }

        server.stop();
    }

    @Test @Ignore
    public void addDirectoryTestsWalk() throws IOException, InterruptedException{
        final int port = 25004;
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final FileHandler handler = new FileHandler();

        final String testRealFile = "test.txt", testFileContent = "readme";
        final String readDir = "dirnoread";
        final String innerFile = "innerNoRead.txt";

        final File noWalk = new File("src/test/resources/directory/nowalk");

        final String context = "";

        // handler.addDirectory(noWalk,true);
        handler.addDirectory(noWalk, ByteLoadingOption.LIVELOAD,true); // todo: def liveload for walk param

        server.createContext(context,handler);
        server.start();

        final String[] validPathsToTest = { // valid reads
            noWalk.getName() + '/' + testRealFile,
            noWalk.getName() + '/' + readDir + '/' + innerFile
        };

        for(final String path : validPathsToTest){
            String url = "http://localhost:" + port + '/' + path;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            try{
                String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNotNull("Client did not find data for " + path,response);
                Assert.assertEquals("Client data did not match server data for " + path,testFileContent,response);
            }catch(ExecutionException e){
                Assert.fail("Client did not find data for " + path);
            }
        }

        final String[] invalidPathsToTest = {
            noWalk.getName() + '/' + readDir
        };

        for(final String path : invalidPathsToTest){
            String url = "http://localhost:" + port + '/' +  path;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            Exception exception = null;
            try{
                String response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();

                Assert.assertNull("Client found data for blocked path " + path,response);
            }catch(ExecutionException e){
                exception = e;
            }
            Assert.assertNotNull("Client found data for blocked path",exception);
        }

        server.stop();
    }

}
