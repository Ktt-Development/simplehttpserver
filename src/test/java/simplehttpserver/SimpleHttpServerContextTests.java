package simplehttpserver;

import com.kttdevelopment.simplehttpserver.*;
import com.kttdevelopment.simplehttpserver.handler.RootHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SimpleHttpServerContextTests {

    @Test
    public void randContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String initContext = server.getRandomContext();
        Assert.assertNotNull("Random context may not be null",initContext);
        server.createContext(initContext);
        for(int i = 0; i < 100; i++)
            Assert.assertNotEquals("Random context may not be a duplicate",initContext,server.getRandomContext());
    }

    @Test
    public void randContextHead() throws IOException{
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
    public void removeNullContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        Exception exception = null;
        try{ server.removeContext((String) null);
        }catch(final NullPointerException e){ exception = e; }
        Assert.assertNotNull("Null string context should throw NPE", exception);

        String context = "";
        exception = null;
        try{ server.removeContext(context);
        }catch(final IllegalArgumentException e){ exception = e; }
        Assert.assertNotNull("Server should throw IllegalArgumentException when removing a context that doesn't exist", exception);
    }

    @Test
    public void removeContext() throws IOException{
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
    public void removeNativeContext() throws IOException{
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
    public void createRootContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final RootHandler handler = new RootHandler((SimpleHttpHandler) SimpleHttpExchange::close, (SimpleHttpHandler) SimpleHttpExchange::close);

        String context = server.getRandomContext();
        Exception exception = null;
        try{
            server.createContext(context,handler);
        }catch(final IllegalArgumentException e){ exception = e; }
        Assert.assertNotNull("Server should throw IllegalArgumentException when adding RootHandler to non-root context",exception);

        final String[] testRoots = {"/","\\",""};

        for(final String testRoot : testRoots){
            try{
                server.removeContext(server.createContext(testRoot, handler));
            }catch(final IllegalArgumentException e){
                Assert.fail("Server threw IllegalArgumentException for allowed context [" + testRoot + "] for RootHandler");
            }
        }
    }

    @Test
    public void createSlashedContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final String[] roots = {"/","\\",""};

        for(final String root : roots)
            Assert.assertEquals("Context [" + root + "] should correct to \"/\"","/",server.createContext(root).getPath());
    }

}
