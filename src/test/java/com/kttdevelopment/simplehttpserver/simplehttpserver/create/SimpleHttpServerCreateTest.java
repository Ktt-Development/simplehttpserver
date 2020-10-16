package com.kttdevelopment.simplehttpserver.simplehttpserver.create;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

public final class SimpleHttpServerCreateTest {

    @Test
    public final void create() throws IOException{
        final int port = 8080;
        SimpleHttpServer server  = SimpleHttpServer.create();
        Assertions.assertThrows(IllegalStateException.class, server::start, "Start server with no port should throw an exception");

        server.bind(port);
        server.stop();
        Assertions.assertDoesNotThrow((Executable) server::stop, "Stopping already stopped server should not throw an exception");
    }

}
