package com.kttdevelopment.simplehttpserver.simplehttpserver.bind;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.*;

import java.io.IOException;
import java.net.BindException;

public final class SimpleHttpServerBindOccupiedTests {

    @Test
    public final void testOccupiedPortBind() throws IOException{
        final int port = 8080;
        final SimpleHttpServer s1 = SimpleHttpServer.create(port);
        s1.start();

        Exception exception = null;
        try{ SimpleHttpServer.create(port);
        }catch(final BindException e){ exception = e; }

        Assert.assertNotNull("Bind server to occupied port should throw an exception",exception);

        s1.stop();
        exception = null;
        try{ SimpleHttpServer.create(port);
        }catch(final BindException e){ exception = e; }
        Assert.assertNull("Bind server to now unoccupied port should not throw an exception",exception);
    }

}
