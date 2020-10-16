package com.kttdevelopment.simplehttpserver.simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public final class SimpleHttpServerGetTests {

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public final void get() throws IOException{
        final int port = 8080;

        final SimpleHttpServer server = SimpleHttpServer.create();
        Assertions.assertNotNull(server.getHttpServer(), "#getHttpServer() should not be null");

        Assertions.assertNull(server.getAddress(), "#getAddress() should be null for unbinded server");
        server.bind(port);
        Assertions.assertTrue(server.getAddress().getAddress().isAnyLocalAddress(), "#getAddress() should be the local address of the server running it");

        Assertions.assertNull(server.getExecutor(), "#getExecutor() should initially be null");
        Assertions.assertNull(server.getHttpSessionHandler(), "#getHttpSessionHandler() should initially be null");
        Assertions.assertTrue(server.getContexts().isEmpty(), "#getContexts() should initially be empty on server instantiation");
    }

}
