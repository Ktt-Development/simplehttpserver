package com.kttdevelopment.simplehttpserver.simplehttpserver;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.RootHandler;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public final class SimpleHttpServerContextTests {

    @Test
    public final void randContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String initContext = server.getRandomContext();
        Assertions.assertNotNull(initContext, "Random context may not be null");
        server.createContext(initContext);
        for(int i = 0; i < 100; i++)
            Assertions.assertNotEquals(initContext, server.getRandomContext(), "Random context may not be a duplicate");
    }

    @Test
    public final void randContextHead() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String head = "/head";
        final String initContext = server.getRandomContext(head);
        Assertions.assertNotNull(initContext, "Random context may not be null");
        Assertions.assertTrue(initContext.startsWith(head + '/'), "Random context with parameter must have correct head");
        server.createContext(initContext);
        for(int i = 0; i < 100; i++)
            Assertions.assertNotEquals(initContext, server.getRandomContext(head), "Random context may not be a duplicate");
    }

    @Test
    public final void removeNullContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        Assertions.assertThrows(NullPointerException.class, () -> server.removeContext((String) null), "Null string context should throw NPE");
        Assertions.assertThrows(IllegalArgumentException.class, () -> server.removeContext(""), "Server should throw IllegalArgumentException when removing a context that doesn't exist");
    }

    @Test
    public final void removeContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String context = "";
        server.createContext(context);

        Assertions.assertDoesNotThrow(() -> server.removeContext(context), "Server should not throw exception when removing existing string context");
        Assertions.assertDoesNotThrow(() -> server.removeContext(server.createContext(context)), "Server should not throw exception when removing existing http context");
    }

    @Test
    public final void removeNativeContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        String context = "/";

        Assertions.assertDoesNotThrow(() -> server.getHttpServer().createContext(context), "Removing a context added by the native http server should not throw an exception");

        server.createContext(context);
        server.getHttpServer().removeContext(context);

        Assertions.assertDoesNotThrow(() -> server.createContext(context), "Server should be able to create a new context if removed by native http server");
    }

    @Test
    public void createContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        String context = "";

        Assertions.assertNotEquals(server.createContext(context).getHandler(), server.getContextHandler(context), "Handler from #createContext(context)#getHandler() should not be the same when retrieving from #getContextHandler(context) because a wrapper handler is used");
        Assertions.assertEquals(1, server.getContexts().size(), "Server context size should be 1 after 1 added");

        final SimpleHttpHandler handler = SimpleHttpExchange::close;

        context = "2";
        Assertions.assertNotEquals(handler, server.createContext(context, handler).getHandler(), "Handler passed to #createContext(context, handler) should not be the same as #createContext(context, handler)#getHandler() because a wrapper handler is used");
        Assertions.assertEquals(2, server.getContexts().size(), "Server context size should be 2 after 1 added");

        context = "3";
        Assertions.assertEquals(handler, server.getContextHandler(server.createContext(context, handler)), "Handler passed to #createContext(context, handler) should be the same as #getContextHandler(context)");
        Assertions.assertEquals(3, server.getContexts().size(), "Server context size should be 3 after 1 added");
    }

    @Test
    public final void createRootContext() throws IOException{
        final SimpleHttpServer server   = SimpleHttpServer.create();
        final RootHandler handler       = new RootHandler(HttpExchange::close, HttpExchange::close);

        String context = server.getRandomContext();

        Assertions.assertThrows(IllegalArgumentException.class, () -> server.createContext(context, handler), "Server should throw IllegalArgumentException when adding RootHandler to non-root context");

        final String[] testRoots = {"/","\\",""};

        for(final String testRoot : testRoots)
            Assertions.assertDoesNotThrow(() -> server.createContext(testRoot, handler), "Server threw IllegalArgumentException for allowed context [" + testRoot + "] for RootHandler");
    }

    @Test
    public final void createSlashedContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final String[] roots = {"/","\\",""};

        for(final String root : roots)
            Assertions.assertEquals("/", server.createContext(root).getPath(), "Context [" + root + "] should correct to \"/\"");
    }

    @Test// (expected = IllegalArgumentException.class)
    public final void createDuplicateContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final String context = "duplicate";

        server.createContext(context);
        server.createContext(context, HttpExchange::close);
    }

}
