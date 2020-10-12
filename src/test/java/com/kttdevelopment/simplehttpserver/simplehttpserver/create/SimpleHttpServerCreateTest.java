package com.kttdevelopment.simplehttpserver.simplehttpserver.create;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public final class SimpleHttpServerCreateTest {

    @Test
    public final void create() throws IOException{
        final int port = 8080;
        SimpleHttpServer server  = SimpleHttpServer.create();
        try{
            server.start();
            Assert.fail("Start server with no port should throw an exception");
        }catch(final IllegalStateException ignored){ }

        server.bind(port);
        try{
            server.stop();
            server.stop();
        }catch(final IllegalStateException ignored){
            Assert.fail("Start server with valid port should not throw an exception");
        }
    }

}
