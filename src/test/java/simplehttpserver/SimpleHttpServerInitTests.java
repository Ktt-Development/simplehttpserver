package simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SimpleHttpServerInitTests {

    final int port = 8080; // port may clash with other tests

    @Test
    public void create() throws IOException{
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
    }

}
