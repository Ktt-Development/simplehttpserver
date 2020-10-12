package com.kttdevelopment.simplehttpserver.simplehttpserver;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.RootHandler;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public final class SimpleHttpServerContextTests {

    @Test
    public final void randContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String initContext = server.getRandomContext();
        Assert.assertNotNull("Random context may not be null",initContext);
        server.createContext(initContext);
        for(int i = 0; i < 100; i++)
            Assert.assertNotEquals("Random context may not be a duplicate",initContext,server.getRandomContext());
    }

    @Test
    public final void randContextHead() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String head = "/head";
        final String initContext = server.getRandomContext(head);
        Assert.assertNotNull("Random context may not be null",initContext);
        Assert.assertTrue("Random context with parameter must have correct head",initContext.startsWith(head + '/'));
        server.createContext(initContext);
        for(int i = 0; i < 100; i++)
            Assert.assertNotEquals("Random context may not be a duplicate",initContext,server.getRandomContext(head));
    }

    @Test
    public final void removeNullContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        try{
            server.removeContext((String) null);
            Assert.fail("Null string context should throw NPE");
        }catch(final NullPointerException ignored){ }

        try{
            server.removeContext("");
            Assert.fail("Server should throw IllegalArgumentException when removing a context that doesn't exist");
        }catch(final IllegalArgumentException ignored){ }
    }

    @Test
    public final void removeContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String context = "";
        server.createContext(context);
        try{ server.removeContext(context);
        }catch(final IllegalArgumentException ignored){
            Assert.fail("Server should not throw exception when removing existing string context");
        }

        try{ server.removeContext(server.createContext(context));
        }catch(final IllegalArgumentException ignored){
            Assert.fail("Server should not throw exception when removing existing http context");
        }
    }

    @Test
    public final void removeNativeContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        String context = "/";

        try{ server.removeContext(server.getHttpServer().createContext(context));
        }catch(final IllegalArgumentException ignored){
            Assert.fail("Removing a context added by the native http server should not throw an exception");
        }

        server.createContext(context);
        server.getHttpServer().removeContext(context);
        try{
            server.createContext(context);
        }catch(final IllegalArgumentException ignored){
            Assert.fail("Server should be able to create a new context if removed by native http server");
        }
    }

    @Test
    public void createContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        String context = "";

        Assert.assertNotEquals("Handler from #createContext(context)#getHandler() should not be the same when retrieving from #getContextHandler(context) because a wrapper handler is used",server.createContext(context).getHandler(),server.getContextHandler(context));
        Assert.assertEquals("Server context size should be 1 after 1 added",1,server.getContexts().size());

        final SimpleHttpHandler handler = SimpleHttpExchange::close;

        context = "2";
        Assert.assertNotEquals("Handler passed to #createContext(context,handler) should not be the same as #createContext(context,handler)#getHandler() because a wrapper handler is used",handler,server.createContext(context,handler).getHandler());
        Assert.assertEquals("Server context size should be 2 after 1 added",2,server.getContexts().size());

        context = "3";
        Assert.assertEquals("Handler passed to #createContext(context,handler) should be the same as #getContextHandler(context)",handler,server.getContextHandler(server.createContext(context,handler)));
        Assert.assertEquals("Server context size should be 3 after 1 added",3,server.getContexts().size());
    }

    @Test
    public final void createRootContext() throws IOException{
        final SimpleHttpServer server   = SimpleHttpServer.create();
        final RootHandler handler       = new RootHandler(HttpExchange::close,HttpExchange::close);

        String context = server.getRandomContext();
        try{
            server.createContext(context,handler);
            Assert.fail("Server should throw IllegalArgumentException when adding RootHandler to non-root context");
        }catch(final IllegalArgumentException ignored){ }

        final String[] testRoots = {"/","\\",""};

        for(final String testRoot : testRoots)
            try{ server.removeContext(server.createContext(testRoot, handler));
            }catch(final IllegalArgumentException ignored){
                Assert.fail("Server threw IllegalArgumentException for allowed context [" + testRoot + "] for RootHandler");
            }
    }

    @Test
    public final void createSlashedContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final String[] roots = {"/","\\",""};

        for(final String root : roots)
            Assert.assertEquals("Context [" + root + "] should correct to \"/\"","/",server.createContext(root).getPath());
    }


    @Test// (expected = IllegalArgumentException.class)
    public final void createDuplicateContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final String context = "duplicate";

        server.createContext(context);
        server.createContext(context,HttpExchange::close);
    }

}
