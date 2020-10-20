package com.kttdevelopment.simplehttpserver.simplehttpserver.bind;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public final class SimpleHttpServerBindRangeTest {

    @Test
    public void testPortRange() throws IOException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create();

        Assertions.assertThrows(IllegalArgumentException.class, () -> server.bind(-1), "Bind server to bad port (-1) should throw an exception");
        Assertions.assertThrows(IllegalArgumentException.class, () -> server.bind(65536), "Bind server to bad port (65536) should throw an exception");

        Assertions.assertDoesNotThrow(() -> server.bind(port),"Bind server to valid port (" + port + ") should not throw an exception");

        Assertions.assertNotNull(server.getAddress(), "Server address should not be null for successful bind");
        Assertions.assertEquals(port, server.getAddress().getPort(), "Server bind port should equal address port");
    }

}
