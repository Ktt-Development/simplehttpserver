package simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.*;

import java.io.IOException;
import java.net.BindException;
import java.util.concurrent.TimeUnit;

public class SimpleHttpServerBindTests {
    final int port = 10001; // port may clash with other tests

    @Test
    public void testPortRange() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        Exception exception = null;
        try{ server.bind(-1);
        }catch(IllegalArgumentException | IOException e){ exception = e; }
        Assert.assertTrue("Bind server to bad port (-1) should throw an exception", exception instanceof  IllegalArgumentException);

        exception = null;
        try{ server.bind(65536);
        }catch(IllegalArgumentException | IOException e){ exception = e; }
        Assert.assertTrue("Bind server to bad port (65536) should throw an exception", exception instanceof  IllegalArgumentException);

        exception = null;
        try{ server.bind(port);
        }catch(IllegalArgumentException | IOException e){ exception = e; }
        Assert.assertNull("Bind server to valid port (" + port + ") should not throw an exception",exception);

        Assert.assertNotNull("Server address should not be null for successful bind",server.getAddress());
        Assert.assertEquals("Server bind port should equal address port",port,server.getAddress().getPort());
    }

    @Test
    public void testOccupiedPortBind() throws IOException, InterruptedException{
        final SimpleHttpServer s1 = SimpleHttpServer.create(port);
        s1.start();

        Exception exception = null;
        try{ SimpleHttpServer.create(port);
        }catch(final BindException e){ exception = e; }
        s1.stop();

        Assert.assertNotNull("Bind server to occupied port should throw an exception",exception);

        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        exception = null;
        try{ SimpleHttpServer.create(port);
        }catch(final BindException e){ exception = e; }
        Assert.assertNull("Bind server to now unoccupied port should not throw an exception",exception);
    }

}
