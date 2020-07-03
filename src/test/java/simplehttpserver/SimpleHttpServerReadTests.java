package simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SimpleHttpServerReadTests {

    final int port = 82; // port may clash with other tests

    @Test
    public void get() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create(port);
        Assert.assertNotNull("#getHttpServer() should not be null", server.getHttpServer());
        Assert.assertTrue("#getAddress() should noy only be not null, it should also be the local address of the server running it",server.getAddress().getAddress().isAnyLocalAddress());
        Assert.assertNull("#getExecutor() should initially be null",server.getExecutor());
        Assert.assertNull("#getHttpSessionHandler() should initially be null",server.getHttpSessionHandler());
        Assert.assertTrue("#getContexts() should initially be empty on server instantiation",server.getContexts().isEmpty());
    }

}
