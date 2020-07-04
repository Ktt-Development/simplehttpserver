package simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.SimpleHttpsServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SimpleHttpServerInitTests {


    @Test
    public void create() throws IOException{
        final int port = 10003;
        SimpleHttpServer server  = SimpleHttpServer.create();
        Exception exception = null;
        try{
            server.start();
        }catch(final IllegalStateException e){
            exception = e;
        }
        Assert.assertNotNull("Start server with no port should throw an exception", exception);

        server.bind(port);
        server.start();
        server.stop();
    }

    @Test
    public void createHTTPS() throws IOException{
        final int port = 10004;
        SimpleHttpsServer server  = SimpleHttpsServer.create();
        Exception exception = null;
        try{
            server.start();
        }catch(final IllegalStateException e){
            exception = e;
        }
        Assert.assertNotNull("Start server with no port should throw an exception", exception);

        server.bind(port);
        server.start();
        server.stop();
    }

}
