package com.kttdevelopment.simplehttpserver.simplehttpserver.bind;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public final class SimpleHttpServerBindRangeTest {

    @Test
    public void testPortRange() throws IOException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create();

        Exception exception = null;
        try{ server.bind(-1);
        }catch(final IllegalArgumentException | IOException e){ exception = e; }
        Assert.assertTrue("Bind server to bad port (-1) should throw an exception", exception instanceof IllegalArgumentException);

        exception = null;
        try{ server.bind(65536);
        }catch(final IllegalArgumentException | IOException e){ exception = e; }
        Assert.assertTrue("Bind server to bad port (65536) should throw an exception", exception instanceof IllegalArgumentException);

        exception = null;
        try{ server.bind(port);
        }catch(final IllegalArgumentException | IOException e){ exception = e; }
        Assert.assertNull("Bind server to valid port (" + port + ") should not throw an exception",exception);

        Assert.assertNotNull("Server address should not be null for successful bind",server.getAddress());
        Assert.assertEquals("Server bind port should equal address port",port,server.getAddress().getPort());
    }

}
