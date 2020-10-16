package com.kttdevelopment.simplehttpserver.simplehttpserver.bind;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.BindException;

public final class SimpleHttpServerBindOccupiedTests {

    @Test
    public final void testOccupiedPortBind() throws IOException{
        final int port = 8080;
        final SimpleHttpServer s1 = SimpleHttpServer.create(port);
        s1.start();

        Assertions.assertThrows(BindException.class, () -> SimpleHttpServer.create(port), "Bind server to occupied port should throw an exception");

        s1.stop();

        Assertions.assertDoesNotThrow(() -> SimpleHttpServer.create(port), "Bind server to now unoccupied port should not throw an exception");
    }

}
