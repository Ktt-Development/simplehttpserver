package simplehttpserver;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.SimpleHttpsServer;
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

}
