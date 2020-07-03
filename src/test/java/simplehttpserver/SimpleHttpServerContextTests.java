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
        }catch(NullPointerException e){ exception = e; }
        Assert.assertNotNull("Null string context should throw NPE", exception);

        /* broken
        exception = null;
        try{ server.removeContext((HttpContext) null);
        }catch(NullPointerException e){ exception = e; }
        Assert.assertNotNull("Null http context should throw NPE", exception);
        */

        exception = null;
        try{ server.removeContext(server.getRandomContext());
        }catch(IllegalArgumentException e){ exception = e; }
        Assert.assertNotNull("Server should throw IllegalArgumentException when removing a context that doesn't exist", exception);
    }

    @Test
    public void removeContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();

        final String context = server.getRandomContext();
        server.createContext(context);
        try{ server.removeContext(context);
        }catch(IllegalArgumentException e){
            Assert.fail("Server should not throw exception when removing existing string context");
        }

        try{ server.removeContext(server.createContext(server.getRandomContext()));
        }catch(IllegalArgumentException e){
            Assert.fail("Server should not throw exception when removing existing http context");
        }
    }

    @Test
    public void createContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        String context = server.getRandomContext();

        Assert.assertNotEquals("Handler from #createContext(context)#getHandler() should not be the same when retrieving from #getContextHandler(context) because a wrapper handler is used",server.createContext(context).getHandler(),server.getContextHandler(context));
        Assert.assertEquals("Server context size should be 1 after 1 added",1,server.getContexts().size());

        final SimpleHttpHandler handler = SimpleHttpExchange::close;

        context = server.getRandomContext();
        Assert.assertNotEquals("Handler passed to #createContext(context,handler) should not be the same as #createContext(context,handler)#getHandler() because a wrapper handler is used",handler,server.createContext(context,handler).getHandler());
        Assert.assertEquals("Server context size should be 2 after 1 added",2,server.getContexts().size());

        context = server.getRandomContext();
        Assert.assertEquals("Handler passed to #createContext(context,handler) should be the same as #getContextHandler(context)",handler,server.getContextHandler(server.createContext(context,handler)));
        Assert.assertEquals("Server context size should be 3 after 1 added",3,server.getContexts().size());
    }

    @Test
    public void createRootContext() throws IOException{
        final SimpleHttpServer server = SimpleHttpServer.create();
        final RootHandler handler = new RootHandler((SimpleHttpHandler) SimpleHttpExchange::close, (SimpleHttpHandler) SimpleHttpExchange::close);

        Exception exception = null;
        try{
            server.createContext(server.getRandomContext(),handler);
        }catch(IllegalArgumentException e){ exception = e; }
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
