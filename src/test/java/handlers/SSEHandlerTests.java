package handlers;

import com.kttdevelopment.simplehttpserver.SimpleHttpServer;
import com.kttdevelopment.simplehttpserver.handler.SSEHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class SSEHandlerTests {

    @Test @Ignore
    public void test() throws IOException{
        final int port = 30006;

        final SimpleHttpServer server = SimpleHttpServer.create(port);
        final URL target = new URL("http://localhost:" + port);

        final SSEHandler handler = new SSEHandler();
        server.createContext("",handler);
        server.start();



        server.stop();
    }

}
