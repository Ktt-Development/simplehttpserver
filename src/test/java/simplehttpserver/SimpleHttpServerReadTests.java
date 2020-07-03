package simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SimpleHttpServerReadTests {

    final int port = 10003; // port may clash with other tests

    @Test
    public void get() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        Assert.assertNotNull("#getHttpServer() should not be null", server.getHttpServer());

        Assert.assertNull("#getAddress() should be null for unbinded server",server.getAddress());
        server.bind(port);
        Assert.assertTrue("#getAddress() should be the local address of the server running it",server.getAddress().getAddress().isAnyLocalAddress());

        Assert.assertNull("#getExecutor() should initially be null",server.getExecutor());
        Assert.assertNull("#getHttpSessionHandler() should initially be null",server.getHttpSessionHandler());
        Assert.assertTrue("#getContexts() should initially be empty on server instantiation",server.getContexts().isEmpty());
    }

}
